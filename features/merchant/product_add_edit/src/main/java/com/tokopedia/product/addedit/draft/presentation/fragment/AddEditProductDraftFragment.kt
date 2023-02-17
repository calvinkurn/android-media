package com.tokopedia.product.addedit.draft.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.setFragmentToUnifyBgColor
import com.tokopedia.product.addedit.databinding.FragmentAddEditProductDraftBinding
import com.tokopedia.product.addedit.draft.di.AddEditProductDraftComponent
import com.tokopedia.product.addedit.draft.presentation.adapter.ProductDraftListAdapter
import com.tokopedia.product.addedit.draft.presentation.listener.ProductDraftListListener
import com.tokopedia.product.addedit.draft.presentation.viewmodel.AddEditProductDraftViewModel
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.tracking.ProductDraftTracking
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

open class AddEditProductDraftFragment : BaseDaggerFragment(), ProductDraftListListener {

    companion object {
        const val SCREEN_NAME = "/draft product page"
        const val SCREEN_NAME_ADD_PRODUCT = "/add-product"
        const val REQUEST_CODE_ADD_PRODUCT = 9003
        const val FIRST_INDEX = 0
        const val NO_POSITION = -1

        @JvmStatic
        fun newInstance() = AddEditProductDraftFragment()
    }

    @Inject
    lateinit var viewModel: AddEditProductDraftViewModel

    private var binding by autoClearedNullable<FragmentAddEditProductDraftBinding>()
    private var userSession: UserSessionInterface? = null
    private var draftListAdapter: ProductDraftListAdapter? = null
    private var mMenu: Menu? = null
    private var deletePosition = NO_POSITION

    // view references from item_empty_list layout
    // need base_list lib to enable view binding to remove these references
    private var tvEmptyTitle: TextView? = null
    private var tvEmptyContent: TextView? = null
    private var btnAddProduct: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbarActions()
        setHasOptionsMenu(true)
        userSession = UserSession(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run { setup(this) }
        observer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddEditProductDraftBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        mMenu = menu
        inflater.inflate(R.menu.menu_product_draft, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ADD_PRODUCT -> {
                if (resultCode == Activity.RESULT_OK) {
                    ProductDraftTracking.sendScreenProductDraft(screenName, SCREEN_NAME_ADD_PRODUCT)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_label_add_product -> {
                val intent = AddEditProductPreviewActivity.createInstance(context)
                startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
                userSession?.shopId?.run {
                    ProductDraftTracking.sendAddProductClick(this, ProductDraftTracking.CLICK_ADD_PRODUCT_WITHOUT_DRAFT)
                }
            }
            R.id.item_delete_all_draft -> {
                val alertDialogBuilder = AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle)
                    .setMessage(getString(R.string.label_draft_menu_delete_all_draft_dialog_message))
                    .setNegativeButton(getString(R.string.label_cancel_button_on_dialog)) { _, _ -> }
                    .setPositiveButton(getString(R.string.label_delete_button_on_dialog)) { _, _ ->
                        viewModel.deleteAllProductDraft()
                        displayLoader()
                    }
                val dialog = alertDialogBuilder.create()
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(AddEditProductDraftComponent::class.java).inject(this)
    }

    override fun onDraftClickListener(draftId: Long) {
        val intent = AddEditProductPreviewActivity.createInstance(context, draftId.toString())
        startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
        ProductDraftTracking.sendProductDraftClick(ProductDraftTracking.EDIT_DRAFT)
    }

    override fun onDraftDeleteListener(draftId: Long, position: Int, productName: String) {
        val message = if (productName.isBlank()) {
            getString(R.string.label_draft_delete_draft_dialog_message)
        } else {
            getString(R.string.label_draft_delete_draft_dialog_has_product_name_message, productName)
        }
        val alertDialogBuilder = AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle)
            .setMessage(MethodChecker.fromHtml(message))
            .setNegativeButton(getString(R.string.label_cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.label_delete_button_on_dialog)) { _, _ ->
                viewModel.deleteProductDraft(draftId)
                deletePosition = position
                ProductDraftTracking.sendProductDraftClick(ProductDraftTracking.DELETE_DRAFT)
                displayLoader()
            }
        val dialog = alertDialogBuilder.create()
        dialog.show()
    }

    private fun setupToolbarActions() {
        val color = ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N500)
        activity?.findViewById<HeaderUnify>(R.id.toolbar_draft)?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                overflowIcon?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
            } else {
                overflowIcon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            }

            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun setup(binding: FragmentAddEditProductDraftBinding) {
        setFragmentToUnifyBgColor()

        val emptyLayout = binding.emptyLayout
        tvEmptyTitle = emptyLayout.findViewById(com.tokopedia.baselist.R.id.text_view_empty_title_text)
        tvEmptyContent = emptyLayout.findViewById(com.tokopedia.baselist.R.id.text_view_empty_content_text)
        btnAddProduct = emptyLayout.findViewById(com.tokopedia.baselist.R.id.button_add_promo)

        tvEmptyTitle?.text = getString(R.string.label_draft_product_empty)
        tvEmptyContent?.hide()
        btnAddProduct?.apply {
            text = getString(R.string.label_draft_product_add_product)
            setOnClickListener {
                val intent = AddEditProductPreviewActivity.createInstance(context)
                startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
                ProductDraftTracking.sendProductDraftClick(ProductDraftTracking.ADD_PRODUCT)
                userSession?.shopId?.run {
                    ProductDraftTracking.sendAddProductClick(this, ProductDraftTracking.CLICK_ADD_PRODUCT)
                }
            }
        }

        draftListAdapter = ProductDraftListAdapter(this)
        binding.rvDraft.adapter = draftListAdapter
        binding.rvDraft.layoutManager = LinearLayoutManager(context)
        displayLoader()
    }

    private fun observer() {
        observe(viewModel.drafts) {
            dismissLoader()
            when (it) {
                is Success -> {
                    binding?.geDraft?.hide()
                    draftListAdapter?.setDrafts(it.data)
                    displayEmptyListLayout()
                }
                is Fail -> {
                    binding?.rvDraft?.hide()
                    binding?.geDraft?.apply {
                        setType(GlobalError.SERVER_ERROR)
                        setActionClickListener {
                            viewModel.getAllProductDraft()
                            displayLoader()
                        }
                    }?.show()
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }

        observe(viewModel.deleteDraft) {
            dismissLoader()
            when (it) {
                is Success -> {
                    draftListAdapter?.deleteDraft(deletePosition)
                    displayEmptyListLayout()
                }
                is Fail -> {
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }

        observe(viewModel.deleteAllDraft) {
            dismissLoader()
            binding?.frameLayout?.let { toasterParent ->
                when (it) {
                    is Success -> {
                        Toaster.build(
                            toasterParent,
                            getString(R.string.label_draft_success_delete_draft_message),
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_NORMAL
                        ).show()
                        draftListAdapter?.deleteAllDrafts()
                        displayEmptyListLayout()
                    }
                    is Fail -> {
                        Toaster.build(
                            toasterParent,
                            getString(R.string.label_draft_error_delete_draft_message),
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR
                        ).show()
                        AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                    }
                }
            }
        }
    }

    private fun displayLoader() {
        binding?.loaderUnify?.show()
        binding?.rvDraft?.hide()
        binding?.geDraft?.hide()
    }

    private fun dismissLoader() {
        binding?.loaderUnify?.hide()
        binding?.rvDraft?.show()
    }

    private fun displayEmptyListLayout() {
        draftListAdapter?.isDraftEmpty()?.let { isEmpty ->
            if (isEmpty) {
                binding?.emptyLayout?.show()
                binding?.rvDraft?.hide()
                mMenu?.findItem(R.id.item_delete_all_draft)?.isVisible = false
            } else {
                binding?.rvDraft?.show()
                binding?.emptyLayout?.hide()
                mMenu?.findItem(R.id.item_delete_all_draft)?.isVisible = true
            }
        }
    }
}
