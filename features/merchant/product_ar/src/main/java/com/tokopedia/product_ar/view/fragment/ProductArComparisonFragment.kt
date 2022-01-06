package com.tokopedia.product_ar.view.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.modiface.mfemakeupkit.MFEMakeupEngine
import com.modiface.mfemakeupkit.effects.MFEMakeupProduct
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.common.showToasterSuccess
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.di.ProductArComponent
import com.tokopedia.product_ar.model.state.GenerateMakeUpMode
import com.tokopedia.product_ar.model.state.ImageMapMode
import com.tokopedia.product_ar.util.ArGridImageDownloader
import com.tokopedia.product_ar.util.ItemDividerGrid
import com.tokopedia.product_ar.view.ProductArActivity
import com.tokopedia.product_ar.view.ProductArListener
import com.tokopedia.product_ar.view.adapter.PhotoComparisonAdapter
import com.tokopedia.product_ar.view.partialview.PartialBottomArComparisonView
import com.tokopedia.product_ar.viewmodel.ProductArComparisonViewModel
import com.tokopedia.product_ar.viewmodel.ProductArSharedViewModel
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductArComparisonFragment : BaseDaggerFragment(), ComparissonHelperListener, ProductArListener, MFEMakeupEngine.u {

    companion object {
        const val PRODUCT_AR_COMPARISON_FRAGMENT = "product_ar_fragment"
        fun newInstance() = ProductArComparisonFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: ProductArComparisonViewModel? = null
    private var sharedViewModel: ProductArSharedViewModel? = null

    private var rvComparison: RecyclerView? = null
    private var comparisonAdapter: PhotoComparisonAdapter = PhotoComparisonAdapter(this)

    private var bottomComparissonView: PartialBottomArComparisonView? = null
    private var btnTest: UnifyButton? = null
    private var rvHeight: Int = 0
    private var navToolbar: NavToolbar? = null
    private var layoutManager: GridLayoutManager? = null
    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    private var loader: LoaderUnify? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_ar_comparison_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductArComparisonViewModel::class.java)
        activity?.let { activity ->
            sharedViewModel = ViewModelProvider(activity).get(ProductArSharedViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeData()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(ProductArComponent::class.java)?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomComparissonView = PartialBottomArComparisonView.build(view, this)
        loader = view.findViewById(R.id.ar_grid_loader)
        loader?.show()
        initView(view)
        setupNavToolbar()
        setupRv()
    }

    private fun getMakeUpEngine(): MFEMakeupEngine? {
        return (activity as? ProductArActivity)?.getMakeUpEngine()
    }

    override fun onResume() {
        super.onResume()
        getMakeUpEngine()?.onResume(requireContext())
        setEngineCallback(true)
    }

    override fun onPause() {
        getMakeUpEngine()?.onPause()
        super.onPause()
    }

    private fun observeData() {
        observeInitialData()
        observeVariantRvData()
        observeGenerateMakeUpLook()

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel?.addRemoveImageGrid?.collectLatest {
                if (it.imagesBitmap.isEmpty()) return@collectLatest

                if (it.mode == ImageMapMode.APPEND) {
                    layoutManager?.spanCount = it.spanSize
                    if (rvComparison?.itemDecorationCount == 0) {
                        rvComparison?.addItemDecoration(ItemDividerGrid())
                    }
                    comparisonAdapter.setData(it.imagesBitmap)
                } else {
                    if (it.imagesBitmap.size == 1) {
                        layoutManager?.spanCount = it.spanSize
                        comparisonAdapter.setData(it.imagesBitmap)
                    } else {
                        comparisonAdapter.removeData(it.removePosition)
                    }
                }
            }
        }
    }

    private fun observeGenerateMakeUpLook() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.generateMakeUpBackground?.collectLatest {
                if (it.mode == GenerateMakeUpMode.SELECTION) {
                    /**
                     * prevent register callback when first time open the page,
                     * cause it will call multiple times
                     */
                    setEngineCallback()
                }
                getMakeUpEngine()?.setMakeupLook(it.mfeMakeupLook)
            }
        }
    }

    private fun observeVariantRvData() {
        viewModel?.processedVariantData?.observe(viewLifecycleOwner) {
            bottomComparissonView?.renderView(it)
        }
    }

    private fun observeInitialData() {
        sharedViewModel?.arListData?.observeOnce(viewLifecycleOwner) {
            getMakeUpEngine()?.startRunningWithPhoto(it.second, false)
            getMakeUpEngine()?.applyMakeupToPhotoInBackground(it.second, false,
                    object : MFEMakeupEngine.ApplyMakeupToPhotoCompletionHandler {
                        override fun onMakeupAppliedToPhoto(p0: Bitmap?, p1: Bitmap?, p2: Throwable?) {
                            activity?.runOnUiThread(Runnable {
                                p1?.let {
                                    loader?.hide()
                                    viewModel?.addGridImages(it, comparisonAdapter.listBitmap)
                                }
                            })
                        }
                    })

            viewModel?.renderInitialData(it.first)
        }
    }

    private fun setEngineCallback(disabled: Boolean = false) {
        getMakeUpEngine()?.setCallback(object : MFEMakeupEngine.u {
            override fun a(p0: Bitmap?, p1: Bitmap?) {
                if (!disabled) {
                    activity?.runOnUiThread {
                        p1?.let {
                            viewModel?.addGridImages(it, comparisonAdapter.listBitmap)
                        }
                    }
                }
            }
        })
    }

    private fun setupRv() {
        layoutManager = GridLayoutManager(context, 1)
        rvComparison?.layoutManager = layoutManager
        rvComparison?.itemAnimator = DefaultItemAnimator()
        rvComparison?.adapter = comparisonAdapter

        rvComparison?.addOneTimeGlobalLayoutListener {
            val rvMargin = rvComparison?.layoutParams as? ViewGroup.MarginLayoutParams
            rvHeight = rvComparison?.height ?: 0 - (rvMargin?.bottomMargin ?: 0)
        }
    }

    private fun initView(view: View) {
        rvComparison = view.findViewById(R.id.rv_ar_comparison)
        btnTest = view.findViewById(R.id.btn_atc_ar_comparison)
        navToolbar = view.findViewById(R.id.product_ar_comparison_toolbar)
    }

    override fun getRecyclerViewFullHeight(): Int {
        return rvHeight
    }

    private fun setupNavToolbar() {
        activity?.let { activity ->
            navToolbar?.run {
                setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
                setToolbarTitle("")
                setupToolbarWithStatusBar(activity, NavToolbar.Companion.StatusBar.STATUS_BAR_LIGHT)
            }
        }
    }

    override fun onVariantClicked(productId: String, isSelected: Boolean, selectedMfeProduct: MFEMakeupProduct) {
        viewModel?.setSelectedVariant(
                data = bottomComparissonView?.adapter?.getCurrentArImageDatas() ?: listOf(),
                selectedProductId = productId,
                isSelected = isSelected)
    }

    override fun onButtonClicked(productId: String) {
        ArGridImageDownloader.screenShotAndSaveGridImage(rvComparison as? View,
                viewModel?.addRemoveImageGrid?.value?.imagesBitmap?.size ?: -1) {
            mainThreadHandler.postDelayed({
                bottomComparissonView?.stopButtonLoading()
                val messageSuccess = context?.getString(R.string.message_success_save_photo_to_gallery)
                        ?: ""
                view?.showToasterSuccess(messageSuccess)
            }, 500)
        }
    }

    override fun a(p0: Bitmap?, p1: Bitmap?) {
        activity?.runOnUiThread {
            p1?.let {
                viewModel?.addGridImages(it, comparisonAdapter.listBitmap)
            }
        }
    }
}

interface ComparissonHelperListener {
    fun getRecyclerViewFullHeight(): Int
}