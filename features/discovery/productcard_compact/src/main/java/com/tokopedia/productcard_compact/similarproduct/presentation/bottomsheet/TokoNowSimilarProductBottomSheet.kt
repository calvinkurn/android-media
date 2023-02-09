package com.tokopedia.productcard_compact.similarproduct.presentation.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.productcard_compact.databinding.BottomsheetTokopedianowSimilarProductsBinding
import com.tokopedia.productcard_compact.similarproduct.presentation.adapter.SimilarProductAdapter
import com.tokopedia.productcard_compact.similarproduct.presentation.adapter.SimilarProductAdapterTypeFactory
import com.tokopedia.productcard_compact.similarproduct.presentation.listener.TokoNowSimilarProductTrackerListener
import com.tokopedia.productcard_compact.similarproduct.presentation.uimodel.SimilarProductUiModel
import com.tokopedia.productcard_compact.similarproduct.presentation.viewholder.SimilarProductViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.productcard_compact.R
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoNowSimilarProductBottomSheet : BottomSheetUnify() {

    companion object {

        fun newInstance(): TokoNowSimilarProductBottomSheet {
            return TokoNowSimilarProductBottomSheet()
        }

        private val TAG: String = TokoNowSimilarProductBottomSheet::class.java.simpleName
    }

    var triggerProductId = ""
    var items: List<Visitable<*>> = emptyList()
        set(value) {
            field = value
            submitList(value)
        }
    var productListener: SimilarProductViewHolder.SimilarProductListener? = null

    private var listener: TokoNowSimilarProductTrackerListener? = null

    private var binding by autoClearedNullable<BottomsheetTokopedianowSimilarProductsBinding>()

    private val adapter by lazy {
        SimilarProductAdapter(
            SimilarProductAdapterTypeFactory(
                productListener = productListener
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
        setupRecyclerView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    private fun configureBottomSheet() {
        clearContentPadding = true
        showCloseIcon = true
        isHideable = true
        showKnob = true
        setCloseClickListener {
            dismiss()
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = this@TokoNowSimilarProductBottomSheet.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun submitList(value: List<Visitable<*>>) {
        adapter.submitList(value)
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

    fun showEmptyProductListUi(){
        binding?.viewFlipper?.displayedChild = 1
    }

    fun updateMiniCart(shopId: String){
        binding?.miniCart?.updateData(listOf(shopId))
    }

    fun setMiniCartData(data: MiniCartSimplifiedData, shopId: Long, listener: MiniCartWidgetListener) {
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
        }
    }

    fun changeQuantity(quantity:Int, position: Int){
        val item = (items[position] as SimilarProductUiModel)
        val newItems = items.toMutableList()
        newItems[position] = item.copy(quantity = quantity)
        items = newItems
    }

    fun setListener(listener: TokoNowSimilarProductTrackerListener?){
        this.listener = listener
    }

    fun openMiniCartBottomsheet(fragment: Fragment){
        binding?.miniCart?.showMiniCartListBottomSheet(fragment)
    }
}
