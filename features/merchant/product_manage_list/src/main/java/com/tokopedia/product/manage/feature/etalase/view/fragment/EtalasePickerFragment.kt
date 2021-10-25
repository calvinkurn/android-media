package com.tokopedia.product.manage.feature.etalase.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.util.ProductManageListErrorHandler
import com.tokopedia.product.manage.databinding.ProductManageEtalasePickerBinding
import com.tokopedia.product.manage.feature.etalase.data.model.EtalaseViewModel
import com.tokopedia.product.manage.feature.etalase.di.DaggerProductManageEtalaseComponent
import com.tokopedia.product.manage.feature.etalase.di.ProductManageEtalaseComponent
import com.tokopedia.product.manage.feature.etalase.di.ProductManageEtalaseModule
import com.tokopedia.product.manage.feature.etalase.view.adapter.EtalaseAdapter
import com.tokopedia.product.manage.feature.etalase.view.adapter.viewholder.EtalaseViewHolder
import com.tokopedia.product.manage.feature.etalase.view.viewmodel.EtalasePickerViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class EtalasePickerFragment: Fragment(), EtalaseViewHolder.OnClickListener,
    HasComponent<ProductManageEtalaseComponent> {

    companion object {
        const val EXTRA_ETALASE_ID = "extra_etalase_id"
        const val EXTRA_ETALASE_NAME = "extra_etalase_name"
        const val PARAM_ADD_ETALASE_SUCCESS = "IS_SUCCESS"

        const val REQUEST_CODE_PICK_ETALASE = 1010
        const val REQUEST_CODE_ADD_ETALASE = 1020
    }

    @Inject
    lateinit var viewModel: EtalasePickerViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private val adapter by lazy { EtalaseAdapter(this) }

    private var binding by autoClearedNullable<ProductManageEtalasePickerBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ProductManageEtalasePickerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependency()
        setHasOptionsMenu(true)

        setupSearchBar()
        setupEtalaseList()
        setupSaveButton()
        setupEmptyState()
        observeGetEtalase()

        getEtalaseList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_product_manage_etalase, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        goToAddEtalasePage()
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_ETALASE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getBooleanExtra(PARAM_ADD_ETALASE_SUCCESS, false)) {
                viewModel.clearGetEtalaseCache()
                getEtalaseList(withDelay = true)
            }
        }
    }

    private fun injectDependency() {
        component?.inject(this)
    }

    override fun getComponent(): ProductManageEtalaseComponent? {
        return activity?.run {
            DaggerProductManageEtalaseComponent.builder()
                .productManageEtalaseModule(ProductManageEtalaseModule())
                .productManageComponent(ProductManageInstance.getComponent(application))
                .build()
        }
    }

    override fun onClickEtalase(isChecked: Boolean, etalase: EtalaseViewModel) {
        togglePreviousSelectedEtalase(etalase)
        setCurrentSelectedEtalase(isChecked, etalase)
        binding?.btnSave?.isEnabled = isChecked
    }

    private fun setupSearchBar() {
        binding?.searchBar?.clearFocus()

        binding?.searchBar?.searchBarTextField?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyword = binding?.searchBar?.searchBarTextField?.text?.toString().orEmpty()

                if(keyword.isNotEmpty()) {
                    clearSelection()
                    performSearch(keyword)
                } else {
                    resetSearch()
                }

                return@setOnEditorActionListener true
            }
            false
        }

        binding?.searchBar?.searchBarIcon?.setOnClickListener {
            resetSearch()
        }
    }

    private fun performSearch(keyword: String) {
        val searchResult = adapter.list.filterIsInstance<EtalaseViewModel>()
            .filter { it.name.contains(keyword, true) }
        showEtalaseList(searchResult)
    }

    private fun resetSearch() {
        clearSearchBar()
        clearSelection()
        getEtalaseList()
    }

    private fun setupEtalaseList() {
        binding?.etalaseList?.adapter = adapter
    }

    private fun setupEmptyState() {
        binding?.emptyState?.run {
            errorTitle.text = getString(R.string.product_manage_etalase_picker_empty_title)
            errorDescription.text = getString(R.string.product_manage_etalase_picker_empty_description)
            errorAction.text = getString(R.string.product_manage_add_etalase)
            setActionClickListener {
                goToAddEtalasePage()
            }
        }
    }

    private fun goToAddEtalasePage() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ETALASE_ADD)
        startActivityForResult(intent, REQUEST_CODE_ADD_ETALASE)
    }

    private fun getEtalaseList(withDelay: Boolean = false) {
        val shopId = userSession.shopId
        viewModel.getEtalaseList(shopId, withDelay)
    }

    private fun setupSaveButton() {
        binding?.btnSave?.setOnClickListener {
            finishPickEtalase()
        }
    }

    private fun finishPickEtalase() {
        activity?.run {
            viewModel.selectedEtalase?.let { selectedEtalase ->
                val intent = Intent().apply {
                    putExtra(EXTRA_ETALASE_ID, selectedEtalase.id)
                    putExtra(EXTRA_ETALASE_NAME, selectedEtalase.name)
                }
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    private fun setCurrentSelectedEtalase(isChecked: Boolean, etalase: EtalaseViewModel) {
        if(isChecked) {
            viewModel.selectedEtalase = etalase
        } else {
            viewModel.selectedEtalase = null
        }
    }

    private fun clearSearchBar() {
        binding?.searchBar?.searchBarTextField?.text?.clear()
    }

    private fun clearSelection() {
        uncheckAllEtalase()
        binding?.btnSave?.isEnabled = false
        viewModel.selectedEtalase = null
    }

    private fun uncheckAllEtalase() {
        for (i in 0..adapter.itemCount) {
            val viewHolder = binding?.etalaseList?.findViewHolderForAdapterPosition(i)
            (viewHolder as? EtalaseViewHolder)?.uncheckEtalase()
        }
    }

    private fun togglePreviousSelectedEtalase(etalase: EtalaseViewModel) {
        viewModel.selectedEtalase?.let {
            if(it != etalase) {
                val viewHolder = binding?.etalaseList?.findViewHolderForAdapterPosition(it.position)
                (viewHolder  as? EtalaseViewHolder)?.toggleEtalase()
            }
        }
    }

    private fun showEtalaseList(etalaseList: List<EtalaseViewModel>) {
        adapter.clearAllElements()
        adapter.addElement(etalaseList)
    }

    private fun observeGetEtalase() {
        observe(viewModel.getEtalaseResult) {
            when(it) {
                is Success -> onGetEtalaseSuccess(it.data)
                is Fail -> {
                    showErrorToast()
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun onGetEtalaseSuccess(etalase: List<EtalaseViewModel>) {
        if(etalase.isNotEmpty()) {
            binding?.btnSave?.show()
            showEtalaseList(etalase)
        } else {
            binding?.btnSave?.hide()
            binding?.emptyState?.show()
        }
    }

    private fun showErrorToast() {
        view?.let {
            val message = getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_fail)
            val actionText = getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_retry)
            Toaster.build(it, message, Toaster.TYPE_ERROR, Toaster.LENGTH_LONG, actionText, View.OnClickListener {
                getEtalaseList()
            }).show()
        }
    }
}