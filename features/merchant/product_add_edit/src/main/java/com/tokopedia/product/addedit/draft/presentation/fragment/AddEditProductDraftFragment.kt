package com.tokopedia.product.addedit.draft.presentation.fragment

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.BROADCAST_ADD_PRODUCT
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
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
import kotlinx.android.synthetic.main.fragment_add_edit_product_draft.*
import javax.inject.Inject

class AddEditProductDraftFragment : BaseDaggerFragment(), ProductDraftListListener {

    companion object {
        const val SCREEN_NAME = "/draft product page"
        const val SCREEN_NAME_ADD_PRODUCT = "/add-product"
        const val REQUEST_CODE_ADD_PRODUCT = 9003
        const val FIRST_INDEX = 0

        @JvmStatic
        fun newInstance() = AddEditProductDraftFragment()
    }

    @Inject
    lateinit var viewModel: AddEditProductDraftViewModel

    private var broadcastReceiver: BroadcastReceiver? = null
    private var tvEmptyTitle: TextView? = null
    private var tvEmptyContent: TextView? = null
    private var btnAddProduct: Button? = null
    private var userSession: UserSessionInterface? = null
    private var draftListAdapter: ProductDraftListAdapter? = null
    private var mMenu: Menu? = null
    private var deletePosition = -1
    private var draftListChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbarActions()
        setHasOptionsMenu(true)
        userSession = UserSession(context)
    }

    override fun onResume() {
        super.onResume()
        registerDraftReceiver()
    }

    override fun onPause() {
        super.onPause()
        unregisterDraftReceiver()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_draft, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        mMenu = menu
        inflater.inflate(R.menu.menu_product_draft, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_CODE_ADD_PRODUCT -> {
                if (resultCode == Activity.RESULT_OK) {
                    ProductDraftTracking.sendScreenProductDraft(screenName, SCREEN_NAME_ADD_PRODUCT)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
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
            headerTitle = getString(R.string.label_title_draft_product)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                overflowIcon?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
            }else{
                overflowIcon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            }

            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun setup() {
        requireActivity().window.decorView.setBackgroundColor(
                ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))

        tvEmptyTitle = activity?.findViewById(com.tokopedia.baselist.R.id.text_view_empty_title_text)
        tvEmptyContent = activity?.findViewById(com.tokopedia.baselist.R.id.text_view_empty_content_text)
        btnAddProduct = activity?.findViewById(com.tokopedia.baselist.R.id.button_add_promo)

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
        rvDraft.adapter = draftListAdapter
        rvDraft.layoutManager = LinearLayoutManager(context)

        viewModel.getAllProductDraft()
        displayLoader()
    }

    private fun observer() {
        observe(viewModel.drafts) {
            dismissLoader()
            when(it) {
                is Success -> {
                    geDraft.hide()
                    draftListAdapter?.setDrafts(it.data)
                    displayEmptyListLayout()
                }
                is Fail -> {
                    rvDraft.hide()
                    geDraft.apply {
                        setType(GlobalError.SERVER_ERROR)
                        setActionClickListener {
                            viewModel.getAllProductDraft()
                            displayLoader()
                        }
                    }.show()
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }

        observe(viewModel.deleteDraft) {
            dismissLoader()
            when(it) {
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
            when(it) {
                is Success -> {
                    Toaster.build(
                            frameLayout,
                            getString(R.string.label_draft_success_delete_draft_message),
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_NORMAL
                    ).show()
                    draftListAdapter?.deleteAllDrafts()
                    displayEmptyListLayout()
                }
                is Fail -> {
                    Toaster.build(
                            frameLayout,
                            getString(R.string.label_draft_error_delete_draft_message),
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR
                    ).show()
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun displayLoader() {
        loaderUnify.show()
        rvDraft.hide()
        geDraft.hide()
    }

    private fun dismissLoader() {
        loaderUnify.hide()
        rvDraft.show()
    }

    private fun displayEmptyListLayout() {
        draftListAdapter?.isDraftEmpty()?.let { isEmpty ->
            if (isEmpty) {
                emptyLayout.show()
                rvDraft.hide()
                mMenu?.findItem(R.id.item_delete_all_draft)?.isVisible = false
            } else {
                rvDraft.show()
                emptyLayout.hide()
                mMenu?.findItem(R.id.item_delete_all_draft)?.isVisible = true
            }
        }
    }

    private fun registerDraftReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == BROADCAST_ADD_PRODUCT) {
                    viewModel.getAllProductDraft()
                    draftListChanged = true;
                }
            }
        }

        activity?.let {
            val intentFilters = IntentFilter().apply {
                addAction(BROADCAST_ADD_PRODUCT)
            }
            broadcastReceiver?.let { receiver ->
                LocalBroadcastManager.getInstance(it).registerReceiver(receiver, intentFilters)
            }
        }
    }

    private fun unregisterDraftReceiver() {
        activity?.let {
            broadcastReceiver?.let { receiver ->
                LocalBroadcastManager.getInstance(it).unregisterReceiver(receiver)
            }
        }
    }

    fun getDraftListChanged(): Boolean {
        return draftListChanged
    }
}