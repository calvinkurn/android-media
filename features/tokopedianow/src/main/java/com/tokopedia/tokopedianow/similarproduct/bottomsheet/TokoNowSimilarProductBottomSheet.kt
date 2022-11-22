package com.tokopedia.tokopedianow.similarproduct.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.BottomSheetUtil.configureMaxHeight
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowSimilarProductsBinding
import com.tokopedia.tokopedianow.similarproduct.adapter.SimilarProductAdapter
import com.tokopedia.tokopedianow.similarproduct.adapter.SimilarProductAdapterTypeFactory
import com.tokopedia.tokopedianow.similarproduct.analytic.ProductAnalytics
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel
import com.tokopedia.tokopedianow.similarproduct.viewholder.SimilarProductViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoNowSimilarProductBottomSheet : BottomSheetUnify() {

    companion object {

        fun newInstance(): TokoNowSimilarProductBottomSheet {
            return TokoNowSimilarProductBottomSheet()
        }

        private val TAG: String = TokoNowSimilarProductBottomSheet::class.java.simpleName
    }

    var items: List<Visitable<*>> = emptyList()
        set(value) {
            field = value
            submitList(value)
        }
    var productListener: SimilarProductViewHolder.SimilarProductListener? = null
    var productAnalytics: ProductAnalytics? = null

    private var binding by autoClearedNullable<BottomsheetTokopedianowSimilarProductsBinding>()

    private val adapter by lazy {
        SimilarProductAdapter(
            SimilarProductAdapterTypeFactory(
                productListener = productListener,
                productAnalytics = productAnalytics
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetTokopedianowSimilarProductsBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        configureBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureMaxHeight(true)
        setupRecyclerView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    fun showToaster(
        message: String,
        duration: Int = Toaster.LENGTH_SHORT,
        type: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        onClickAction: View.OnClickListener = View.OnClickListener { }
    ) {
        Toaster.toasterCustomBottomHeight = context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_40
        ).toIntSafely()
        view?.rootView?.let {
            if (message.isNotBlank()) {
                val toaster = Toaster.build(
                    view = it,
                    text = message,
                    duration = duration,
                    type = type,
                    actionText = actionText,
                    clickListener = onClickAction
                )
                toaster.show()
            }
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun configureBottomSheet() {
        clearContentPadding = true
        showCloseIcon = true
        isDragable = true
        isHideable = true
        showKnob = true
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = this@TokoNowSimilarProductBottomSheet.adapter
            layoutManager = LinearLayoutManager(context)
        }
        submitList(items)
    }

    private fun submitList(value: List<Visitable<*>>) {
        adapter.submitList(value)
    }

    fun showEmptyProductListUi(){
        binding?.viewFlipper?.displayedChild = 1
    }

    fun updateMiniCart(shopId: String){
        binding?.miniCart?.updateData(listOf(shopId))
    }

    fun showMiniCart(data: MiniCartSimplifiedData, shopId: Long, listener: MiniCartWidgetListener) {
        val miniCartWidget = binding?.miniCart
        val showMiniCartWidget = data.isShowMiniCartWidget

        if(showMiniCartWidget) {
            val pageName = MiniCartAnalytics.Page.HOME_PAGE
            val shopIds = listOf(shopId.toString())
            val source = MiniCartSource.TokonowHome
            miniCartWidget?.initialize(
                shopIds = shopIds,
                fragment = this,
                listener = listener,
                pageName = pageName,
                source = source
            )
            miniCartWidget?.show()
        } else {
            hideMiniCart()
        }
        setupPadding(data)
    }

    fun hideMiniCart() {
        binding?.miniCart?.apply {
            hide()
        }
        resetPadding()
    }

    private fun setupPadding(data: MiniCartSimplifiedData) {
        binding?.miniCart?.post {
            val paddingZero = context?.resources?.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl0
            ).orZero()

            val paddingBottom = if (data.isShowMiniCartWidget) {
                getMiniCartHeight()
            } else {
                paddingZero
            }
            binding?.recyclerView?.setPadding(paddingZero, paddingZero, paddingZero, paddingBottom)
        }
    }

    private fun resetPadding() {
        val paddingZero = context?.resources?.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.layout_lvl0
        ).orZero()
        binding?.recyclerView?.setPadding(paddingZero, paddingZero, paddingZero, paddingZero)
    }

    private fun getMiniCartHeight(): Int {
        val space16 = context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_16
        )?.toInt().orZero()
        return binding?.miniCart?.height.orZero() - space16
    }

    fun changeQuantity(quantity:Int, position: Int){
        val item = (items[position] as SimilarProductUiModel)
        item.quantity = quantity
        adapter.notifyItemChanged(position)
    }

    fun updateList(indexList: ArrayList<Int>) {
        indexList.forEach {
            adapter.notifyItemChanged(it)
        }
    }

}
