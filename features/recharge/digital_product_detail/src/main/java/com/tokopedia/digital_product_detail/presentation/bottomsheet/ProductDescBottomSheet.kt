package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.databinding.BottomSheetProductDescBinding
import com.tokopedia.digital_product_detail.presentation.adapter.ProductDescAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens

class ProductDescBottomSheet: BottomSheetUnify() {

    private lateinit var listener: RechargeBuyWidgetListener
    private lateinit var denomData: DenomData

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
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

    fun setListener(listenerBuyWidget: RechargeBuyWidgetListener) {
        this.listener = listenerBuyWidget
    }

    private fun initView() {

        isFullpage = false
        isDragable = false
        showCloseIcon = true

        binding = BottomSheetProductDescBinding.inflate(LayoutInflater.from(context))
        binding?.run {
            if (::denomData.isInitialized) {
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

                if (!denomData?.title.isNullOrEmpty()) {
                    tgTitleProductDesc.show()
                    tgTitleProductDesc.text = denomData?.title
                } else tgTitleProductDesc.hide()

                if (!denomData?.greenLabel.isNullOrEmpty()) {
                    tickerWidgetProductDesc.show()
                    tickerWidgetProductDesc.setText(denomData?.greenLabel)
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

                if (::listener.isInitialized) {
                    buyWidgetProductDesc.showBuyWidget(denomData, listener)
                }
            }
        }
        setTitle(getString(R.string.bottom_sheet_prod_desc_title))
        setChild(binding?.root)
    }

    companion object {
        private const val SAVED_INSTANCE_MANAGER_ID = "SAVED_INSTANCE_MANAGER_ID"
        private const val SAVED_DENOM_DATA = "SAVED_DENOM_DATA"

        fun getInstance(): ProductDescBottomSheet = ProductDescBottomSheet()
    }
}