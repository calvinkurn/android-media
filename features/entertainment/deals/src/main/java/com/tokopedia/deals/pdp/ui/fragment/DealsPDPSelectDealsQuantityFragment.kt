package com.tokopedia.deals.pdp.ui.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.databinding.FragmentDealsDetailSelectQuantityBinding
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPSelectQuantityViewModel
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlin.math.min

class DealsPDPSelectDealsQuantityFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DealsPDPSelectQuantityViewModel::class.java)
    }

    private var productDetailData: ProductDetailData? = null
    private var binding by autoClearedNullable<FragmentDealsDetailSelectQuantityBinding>()
    private var toolbar: HeaderUnify? = null
    private var ivBrand: ImageView? = null
    private var tgTitle: Typography? = null
    private var tgBrandName: Typography? = null
    private var quantityEditor: QuantityEditorUnify? = null
    private var tgMrp: Typography? = null
    private var tgSalesPrice: Typography? = null
    private var tgTotalAmount: Typography? = null

    override fun initInjector() {
        getComponent(DealsPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        productDetailData = arguments?.getParcelable<ProductDetailData>(EXTRA_PRODUCT_DATA)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDealsDetailSelectQuantityBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupHeader()
        renderUi()
    }

    private fun setupUi() {
        view?.apply {
            toolbar = binding?.toolbar
            ivBrand = binding?.ivBrand
            tgTitle = binding?.tgDealDetails
            tgBrandName = binding?.tgBrandName
            tgMrp = binding?.tgMrp
            tgSalesPrice = binding?.tgSalesPrice
            tgTotalAmount = binding?.tgTotalAmount
            quantityEditor = binding?.qtyEditor
        }
    }

    private fun setupHeader() {
        toolbar?.headerTitle = context?.resources?.
        getString(com.tokopedia.deals.R.string.deals_pdp_select_number_of_voucher).orEmpty()
        (activity as DealsPDPActivity).setSupportActionBar(toolbar)
        (activity as DealsPDPActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun renderUi() {
        productDetailData?.apply {
            ivBrand?.loadImage(imageApp)
            tgTitle?.text = displayName
            tgBrandName?.text = brand.title
            val mrp = mrp.toLong()
            val salesPrice = salesPrice.toLong()

            if (mrp.isMoreThanZero() && mrp != salesPrice ) {
                tgMrp?.apply {
                    show()
                    text = DealsUtils.convertToCurrencyString(mrp)
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                tgMrp?.hide()
            }

            tgSalesPrice?.text = DealsUtils.convertToCurrencyString(salesPrice)
            setQuantity(minQty)
            updateTotalAmount(salesPrice)
            quantityEditor?.minValue = minQty
            quantityEditor?.maxValue = maxQty
            quantityEditor?.setValueChangedListener { newValue, _, _ ->
                setQuantity(newValue)
                updateTotalAmount(salesPrice)
            }
        }
    }

    private fun getCurrentQuantity(): Int = viewModel.currentQuantity

    private fun setQuantity(qty: Int) {
        viewModel.currentQuantity = qty
    }

    private fun updateTotalAmount(salesPrice: Long) {
        tgTotalAmount?.text = DealsUtils.convertToCurrencyString(getCurrentQuantity() * salesPrice)
    }

    companion object {
        private const val EXTRA_PRODUCT_DATA = "EXTRA_PRODUCT_DATA"

        fun createInstance(data: ProductDetailData): DealsPDPSelectDealsQuantityFragment {
            val fragment = DealsPDPSelectDealsQuantityFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PRODUCT_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }

}