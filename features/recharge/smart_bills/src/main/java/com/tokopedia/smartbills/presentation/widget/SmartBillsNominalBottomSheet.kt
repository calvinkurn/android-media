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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.RechargeProduct
import com.tokopedia.smartbills.di.DaggerSmartBillsComponent
import com.tokopedia.smartbills.presentation.adapter.SmartBillsNominalAdapter
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsNominalBottomSheetViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SmartBillsNominalBottomSheet: BottomSheetUnify() {

    companion object{
        private val TAG = SmartBillsNominalBottomSheet::class.simpleName

        private const val PARAM_MENU_ID = "menu_id"
        private const val PARAM_CATEGORY_ID = "category_id"
        private const val PARAM_OPERATOR_ID = "operator_id"
        private const val PARAM_CLIENT_NUMBER = "client_number"

        fun newInstance(menuId: Int, categoryId:String, opeartorId: String, clientNumber: String):
                SmartBillsNominalBottomSheet {
            return SmartBillsNominalBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt(PARAM_MENU_ID, menuId)
                    putString(PARAM_CATEGORY_ID, categoryId)
                    putString(PARAM_OPERATOR_ID, opeartorId)
                    putString(PARAM_CLIENT_NUMBER, clientNumber)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: SmartBillsNominalBottomSheetViewModel

    private var menuId: Int = 0
    private var platformId: Int = 5
    private var categoryId: String = ""
    private var operator: String = ""
    private var clientNumber: String = ""

    private var recyclerView: RecyclerView? = null
    private var loader: LoaderUnify? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView(inflater, container)
        menuId = arguments?.getInt(PARAM_MENU_ID).orZero()
        categoryId = arguments?.getString(PARAM_CATEGORY_ID).orEmpty()
        operator = arguments?.getString(PARAM_OPERATOR_ID).orEmpty()
        clientNumber = arguments?.getString(PARAM_CLIENT_NUMBER).orEmpty()
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
        super.onDismiss(dialog)
    }

    fun show(fragmentManager: FragmentManager){
        show(fragmentManager, TAG)
    }

    private fun initInjector() {
        DaggerSmartBillsComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?){
        val itemView = inflater.inflate(R.layout.bottomsheet_smart_bills_nominal, container)
        val title = itemView?.context?.getString(R.string.smart_bills_add_bills_product_nominal_bottom_sheet_title) ?: ""
        recyclerView = itemView.findViewById(R.id.rv_sbm_nominal)
        loader = itemView.findViewById(R.id.loader_sbm_nominal_bottom_sheet)
        isFullpage = true
        clearContentPadding = true
        setTitle(title)
        setChild(itemView)
    }

    private fun getNominalTelco(){
        viewModel.getCatalogNominal(viewModel.createCatalogNominal(
                menuId, platformId, operator, clientNumber))
        showLoader()
    }

    private fun observeNominal(){
        observe(viewModel.catalogProduct){
            when(it){
                is Success -> {
                    showNominalCatalogList(viewModel.getProductByCategoryId(it.data.multitabData.productInputs, categoryId))
                }

                is Fail -> {

                }
            }
        }
    }

    private fun showNominalCatalogList(listProduct: List<RechargeProduct>?){
        listProduct?.let {
            hideLoader()
            val adapterNominal = SmartBillsNominalAdapter()
            adapterNominal.listRechargeProduct = listProduct
            recyclerView?.let {
                it.adapter = adapterNominal
                it.layoutManager = LinearLayoutManager(context)
                it.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    private fun showLoader(){
        loader?.show()
        recyclerView?.hide()
    }

    private fun hideLoader(){
        loader?.hide()
        recyclerView?.show()
    }
}