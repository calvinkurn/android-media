package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.topupbills.data.MultiCheckoutButtons
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.databinding.BottomSheetProductDescBinding
import com.tokopedia.digital_product_detail.presentation.adapter.ProductDescAdapter
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.listener.RechargeProductDescListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens

class ProductDescBottomSheet : BottomSheetUnify() {

    private var listener: RechargeBuyWidgetListener? = null
    private var listenerProductDesc: RechargeProductDescListener? = null
    private var denomData: DenomData? = null
    private var layoutType: DenomWidgetEnum? = null
    private var productListTitle: String? = null
    private var position: Int? = null
    private var multiCheckoutButtons: List<MultiCheckoutButtons> = listOf()

    private var binding by autoClearedNullable<BottomSheetProductDescBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            if (it.containsKey(SAVED_INSTANCE_MANAGER_ID)) {
                val manager = SaveInstanceCacheManager(
                    requireContext(),
                    it.getString(SAVED_INSTANCE_MANAGER_ID)
                )
                denomData = manager.get(SAVED_DENOM_DATA, DenomData::class.java)
                    ?: DenomData()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        context?.run {
            val manager = SaveInstanceCacheManager(this, true).also {
                it.put(SAVED_DENOM_DATA, denomData)
            }
            outState.putString(SAVED_INSTANCE_MANAGER_ID, manager.id)
        }
    }

    fun setDenomData(denomData: DenomData) {
        this.denomData = denomData
    }

    fun setMultiCheckoutButtons(multiCheckoutButtons: List<MultiCheckoutButtons>) {
        this.multiCheckoutButtons = multiCheckoutButtons
    }

    fun setListener(listenerBuyWidget: RechargeBuyWidgetListener) {
        this.listener = listenerBuyWidget
    }

    fun setProductDescListener(listenerProductDescListener: RechargeProductDescListener) {
        this.listenerProductDesc = listenerProductDescListener
    }

    fun setLayoutType(layoutType: DenomWidgetEnum) {
        this.layoutType = layoutType
    }

    fun setProductListTitle(productListTitle: String) {
        this.productListTitle = productListTitle
    }

    fun setPosition(position: Int) {
        this.position = position
    }

    private fun initView() {
        isFullpage = false
        isDragable = false
        showCloseIcon = true

        binding = BottomSheetProductDescBinding.inflate(LayoutInflater.from(context))
        binding?.run {
            denomData?.let { denomData ->
                rvProductDesc.run {
                    val linearLayoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    val dividerItemDecoration =
                        DividerItemDecoration(context, linearLayoutManager.orientation).apply {
                            setUsePaddingLeft(false)
                        }
                    adapter = ProductDescAdapter(denomData.productDescriptions)
                    layoutManager = linearLayoutManager
                    addItemDecoration(dividerItemDecoration)
                }

                if (!denomData.title.isEmpty()) {
                    tgTitleProductDesc.show()
                    tgTitleProductDesc.text = denomData.title
                } else {
                    tgTitleProductDesc.hide()
                }

                if (!denomData.greenLabel.isEmpty()) {
                    tickerWidgetProductDesc.show()
                    tickerWidgetProductDesc.setText(denomData.greenLabel)
                } else {
                    tickerWidgetProductDesc.hide()
                    rvProductDesc.run {
                        setMargin(
                            getDimens(unifyDimens.unify_space_0),
                            getDimens(unifyDimens.unify_space_0),
                            getDimens(unifyDimens.unify_space_0),
                            getDimens(unifyDimens.layout_lvl9)
                        )
                    }
                }

                listener?.let { listener ->
                    buyWidgetProductDesc.renderBuyWidget(denomData, listener, multiCheckoutButtons) //TODO Change Button
                }
                position?.let { position ->
                    listenerProductDesc?.let { listenerProductDesc ->
                        layoutType?.let { layoutType ->
                            productListTitle?.let { productListTitle ->
                                listenerProductDesc.onImpressProductBottomSheet(
                                    denomData,
                                    layoutType,
                                    productListTitle,
                                    position
                                )
                            }
                        }
                    }
                }
            }
        }
        setTitle(getString(R.string.bottom_sheet_prod_desc_title))
        setChild(binding?.root)
    }

    override fun onDismiss(dialog: DialogInterface) {
        denomData?.let { denomData ->
            layoutType?.let { layoutType ->
                listenerProductDesc?.let { listenerProductDesc ->
                    listenerProductDesc.onCloseProductBottomSheet(denomData, layoutType)
                }
            }
        }
        super.onDismiss(dialog)
    }

    companion object {
        private const val SAVED_INSTANCE_MANAGER_ID = "SAVED_INSTANCE_MANAGER_ID"
        private const val SAVED_DENOM_DATA = "SAVED_DENOM_DATA"

        fun getInstance(): ProductDescBottomSheet = ProductDescBottomSheet()
    }
}
