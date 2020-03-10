package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.R
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.productsheet.ProductSheetComponent
import com.tokopedia.play.ui.productsheet.interaction.ProductSheetInteractionEvent
import com.tokopedia.play.ui.variantsheet.VariantSheetComponent
import com.tokopedia.play.ui.variantsheet.interaction.VariantSheetInteractionEvent
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.type.ProductLineUiModel
import com.tokopedia.play.view.viewmodel.PlayVariantViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 06/03/20
 */
class PlayBottomSheetFragment : BaseDaggerFragment(), CoroutineScope {

    companion object {
        private const val PERCENT_VARIANT_SHEET_HEIGHT = 0.6

        fun newInstance(): PlayBottomSheetFragment {
            return PlayBottomSheetFragment()
        }
    }

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + dispatchers.main

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatchers: CoroutineDispatcherProvider

    private val offset16 by lazy { resources.getDimensionPixelOffset(R.dimen.spacing_lvl4) }

    private lateinit var playViewModel: PlayViewModel
    private lateinit var playVariantViewModel: PlayVariantViewModel

    private lateinit var productSheetComponent: UIComponent<*>
    private lateinit var variantSheetComponent: UIComponent<*>

    private val variantSheetMaxHeight: Int
        get() = (requireView().height * PERCENT_VARIANT_SHEET_HEIGHT).toInt()

    private val playFragment: PlayFragment
        get() = requireParentFragment() as PlayFragment

    override fun getScreenName(): String = "Play Bottom Sheet"

    override fun initInjector() {
        DaggerPlayComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .playModule(PlayModule(requireContext()))
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayViewModel::class.java)
        playVariantViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayVariantViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_bottom_sheet, container, false)
        initComponents(view.findViewById(R.id.coordl_bottom_sheet) as ViewGroup)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeProductSheetContent()
        observeVariantSheetContent()
        observeBottomInsetsState()
    }

    private fun observeProductSheetContent() {
        playViewModel.observableProductSheetContent.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetProductSheet(it)
                        )
            }
        })
    }

    private fun observeVariantSheetContent() {
        playViewModel.observableVariantSheetContent.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetVariantSheet(it)
                        )
            }
        })

        playVariantViewModel.observableProductVariant.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetDynamicVariant(it)
                        )
            }
        })
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(it, it.isAnyShown, it.isAnyHidden))

                val productSheetState = it[BottomInsetsType.ProductSheet]

                if (productSheetState != null && !productSheetState.isPreviousStateSame) {
                    when (productSheetState) {
                        is BottomInsetsState.Hidden -> playFragment.onBottomInsetsViewHidden()
                        is BottomInsetsState.Shown -> pushParentPlayBySheetHeight(productSheetState.estimatedInsetsHeight)
                    }
                }
            }
        })
    }

    private fun setupView(view: View) {

    }

    private fun initComponents(container: ViewGroup) {
        productSheetComponent = initProductSheetComponent(container)
        variantSheetComponent = initVariantSheetComponent(container)

        sendInitState()
    }

    private fun sendInitState() {
        launch(dispatchers.immediate) {
            EventBusFactory.get(viewLifecycleOwner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.Init
            )
        }
    }

    private fun initProductSheetComponent(container: ViewGroup): UIComponent<ProductSheetInteractionEvent> {
        val productSheetComponent = ProductSheetComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            productSheetComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            ProductSheetInteractionEvent.OnCloseProductSheet -> closeProductSheet()
                            is ProductSheetInteractionEvent.OnBuyProduct -> openVariantSheet(it.product, ProductAction.Buy)
                            is ProductSheetInteractionEvent.OnAtcProduct -> openVariantSheet(it.product, ProductAction.AddToCart)
                        }
                    }
        }

        return productSheetComponent
    }

    private fun initVariantSheetComponent(container: ViewGroup): UIComponent<VariantSheetInteractionEvent> {
        val variantSheetComponent = VariantSheetComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            variantSheetComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            VariantSheetInteractionEvent.OnCloseVariantSheet -> closeVariantSheet()
                            is VariantSheetInteractionEvent.OnBuyProduct -> shouldBuyProduct(it.productId)
                            is VariantSheetInteractionEvent.OnAddProductToCart -> shouldAtcProduct(it.productId)
                        }
                    }
        }

        return variantSheetComponent
    }

    private fun closeProductSheet() {
        playViewModel.onHideProductSheet()
    }

    private fun openVariantSheet(product: ProductLineUiModel, action: ProductAction) {
        playViewModel.onShowVariantSheet(variantSheetMaxHeight, product, action)
        playVariantViewModel.getProductVariant(product.id)
    }

    private fun closeVariantSheet() {
        playViewModel.onHideVariantSheet()
    }

    private fun shouldBuyProduct(productId: String) {
        Toaster.make(requireView(), "Product bought", Snackbar.LENGTH_SHORT)
        closeVariantSheet()
    }

    private fun shouldAtcProduct(productId: String) {
        Toaster.make(requireView(), "Product added to cart", Snackbar.LENGTH_SHORT)
        closeVariantSheet()
    }

    private fun pushParentPlayBySheetHeight(productSheetHeight: Int) {
        val requiredMargin = offset16
        playFragment.onBottomInsetsViewShown(getScreenHeight() - (productSheetHeight + requiredMargin))
    }
}