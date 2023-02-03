package com.tokopedia.smartbills.presentation.widget

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.showUnifyError
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.CategoryTelcoType
import com.tokopedia.smartbills.data.RechargeCatalogProductInputMultiTabData
import com.tokopedia.smartbills.data.RechargeProduct
import com.tokopedia.smartbills.di.DaggerSmartBillsComponent
import com.tokopedia.smartbills.presentation.adapter.SmartBillsNominalAdapter
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsAddTelcoViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SmartBillsNominalBottomSheet(private val getNominalCallback: SmartBillsGetNominalCallback): BottomSheetUnify(),
        SmartBillsNominalAdapter.SmartBillNominalListener {

    companion object{
        private val TAG = SmartBillsNominalBottomSheet::class.simpleName

        private const val EMPTY_IMAGE_GLOBAL_ERROR : String = TokopediaImageUrl.EMPTY_IMAGE_GLOBAL_ERROR:String

        private const val PARAM_MENU_ID = "menu_id"
        private const val PARAM_CATEGORY_ID = "category_id"
        private const val PARAM_OPERATOR_ID = "operator_id"
        private const val PARAM_CLIENT_NUMBER = "client_number"
        private const val PARAM_IS_REQUEST_NOMINAL = "is_request_nominal"
        private const val PARAM_CATALOG_PRODUCT = "catalog_product"

        fun newInstance(isRequestNominal:Boolean, catalogProductInput: RechargeCatalogProductInputMultiTabData, menuId: Int, categoryId:String, opeartorId: String, clientNumber: String,
                       getNominalCallback: SmartBillsGetNominalCallback):
                SmartBillsNominalBottomSheet {
            return SmartBillsNominalBottomSheet(getNominalCallback).apply {
                arguments = Bundle().apply {
                    putInt(PARAM_MENU_ID, menuId)
                    putString(PARAM_CATEGORY_ID, categoryId)
                    putString(PARAM_OPERATOR_ID, opeartorId)
                    putString(PARAM_CLIENT_NUMBER, clientNumber)
                    putBoolean(PARAM_IS_REQUEST_NOMINAL, isRequestNominal)
                    putParcelable(PARAM_CATALOG_PRODUCT, catalogProductInput)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: SmartBillsAddTelcoViewModel

    private var menuId: Int = 0
    private var platformId: Int = 5
    private var categoryId: String = ""
    private var operator: String = ""
    private var clientNumber: String = ""
    private var isRequestNominal: Boolean = true
    private var catalogProductInput: RechargeCatalogProductInputMultiTabData =
            RechargeCatalogProductInputMultiTabData()

    private var titleBottomSheet: String = ""
    private var emptyGlobalErrorTitle: String = ""
    private var emptyGlobalErrorDesc: String = ""
    private var emptyGlobalErrorBtnTitle: String = ""

    private var recyclerView: RecyclerView? = null
    private var loader: ViewGroup? = null
    private var globalError: GlobalError? = null
    private var errorViewGroup: ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView(inflater, container)
        menuId = arguments?.getInt(PARAM_MENU_ID).orZero()
        categoryId = arguments?.getString(PARAM_CATEGORY_ID).orEmpty()
        operator = arguments?.getString(PARAM_OPERATOR_ID).orEmpty()
        clientNumber = arguments?.getString(PARAM_CLIENT_NUMBER).orEmpty()
        isRequestNominal = arguments?.getBoolean(PARAM_IS_REQUEST_NOMINAL, true).orTrue()
        catalogProductInput = arguments?.getParcelable(PARAM_CATALOG_PRODUCT) ?: RechargeCatalogProductInputMultiTabData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNominalTelco()
        observeNominal()
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onDismiss(dialog: DialogInterface) {
        getNominalCallback.onCloseNominal()
        super.onDismiss(dialog)
    }

    fun show(fragmentManager: FragmentManager){
        show(fragmentManager, TAG)
    }

    override fun onClickProduct(rechargeProduct: RechargeProduct) {
        dismiss()
        getNominalCallback.onProductClicked(rechargeProduct)
    }

    private fun initInjector() {
        DaggerSmartBillsComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?){
        val itemView = inflater.inflate(R.layout.bottomsheet_smart_bills_nominal, container)

        titleBottomSheet = itemView?.context?.getString(R.string.smart_bills_add_bills_product_nominal_bottom_sheet_title) ?: ""
        emptyGlobalErrorTitle = itemView?.context?.getString(R.string.smart_bills_add_bills_product_nominal_bottom_sheet_empty) ?: ""
        emptyGlobalErrorDesc = itemView?.context?.getString(R.string.smart_bills_add_bills_product_nominal_bottom_sheet_empty_desc) ?: ""
        emptyGlobalErrorBtnTitle = itemView?.context?.getString(R.string.smart_bills_add_bills_product_nominal_bottom_sheet_empty_btn) ?: ""

        recyclerView = itemView.findViewById(R.id.rv_sbm_nominal)
        loader = itemView.findViewById(R.id.loader_sbm_nominal_bottom_sheet)
        errorViewGroup = itemView.findViewById(R.id.view_group_error)
        globalError = itemView.findViewById(R.id.global_error_sbm_nominal_telco)

        isFullpage = false
        clearContentPadding = true
        setTitle(titleBottomSheet)
        setChild(itemView)
    }

    private fun getNominalTelco(){
        showLoader()
        viewModel.getCatalogNominal(isRequestNominal, catalogProductInput,viewModel.createCatalogNominal(
                menuId, platformId, operator))
    }

    private fun observeNominal(){
        viewLifecycleOwner.observe(viewModel.catalogProduct){
            when(it){
                is Success -> {
                    if(it.data.multitabData.productInputs.isNullOrEmpty()){
                        showGlobalError(true)
                    } else {
                        val data = it.data
                        val products = viewModel.getProductByCategoryId(it.data.multitabData.productInputs, CategoryTelcoType.getCategoryString(categoryId))
                        showNominalCatalogList(products)
                        products?.let {
                            getNominalCallback.onNominalLoaded(false, data, products)
                        }
                    }
                }

                is Fail -> {
                    showGlobalError(false, it.throwable)
                }
            }
        }
    }

    private fun showNominalCatalogList(listProduct: List<RechargeProduct>?){
        isFullpage = true
        listProduct?.let { products ->
            hideLoader()
            val adapterNominal = SmartBillsNominalAdapter(this@SmartBillsNominalBottomSheet)
            adapterNominal.listRechargeProduct = products
            recyclerView?.let {
                it.adapter = adapterNominal
                it.layoutManager = LinearLayoutManager(context)
                it.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    private fun showLoader(){
        loader?.show()
        recyclerView?.invisible()
        errorViewGroup?.hide()
        globalError?.hide()
    }

    private fun hideLoader(){
        loader?.hide()
        recyclerView?.show()
        errorViewGroup?.hide()
        globalError?.hide()
    }

    private fun showGlobalError(isEmptyData:Boolean,
                                throwable: Throwable = Throwable()){
        setTitle("")
        loader?.hide()
        recyclerView?.invisible()
        if (isEmptyData){
            errorViewGroup?.hide()
            globalError?.run {
                show()
                errorTitle.text = emptyGlobalErrorTitle
                errorDescription.text = emptyGlobalErrorDesc
                errorIllustration.loadImage(EMPTY_IMAGE_GLOBAL_ERROR)
                errorIllustration.adjustViewBounds = true
                errorAction.text = emptyGlobalErrorBtnTitle
                setActionClickListener {
                    dismiss()
                }
                errorSecondaryAction.hide()
            }

        } else {
            globalError?.hide()
            errorViewGroup?.show()
            errorViewGroup?.showUnifyError(throwable, {
                getNominalTelco()
            },{
                getNominalTelco()
            })
        }
    }

}
