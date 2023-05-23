package com.tokopedia.chooseaccount.view.ocl

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.chooseaccount.R
import com.tokopedia.chooseaccount.data.OclAccount
import com.tokopedia.chooseaccount.databinding.FragmentOclChooseAccountBinding
import com.tokopedia.chooseaccount.di.ChooseAccountComponent
import com.tokopedia.chooseaccount.view.adapter.OclAccountAdapter
import com.tokopedia.chooseaccount.view.listener.OclChooseAccountListener
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sessioncommon.tracker.OclTracker
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class OclChooseAccountFragment: BaseDaggerFragment(), OclChooseAccountListener {

    private var binding by autoClearedNullable<FragmentOclChooseAccountBinding>()

    protected var adapter: OclAccountAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            OclChooseAccountViewModel::class.java
        )
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ChooseAccountComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOclChooseAccountBinding.inflate(inflater, container, false)
        adapter = OclAccountAdapter()
        adapter?.setListener(this)
        binding?.oclChooseAccountList?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(OclItemDivider(requireContext()))
        }.also {
            it?.adapter = adapter
        }
        return binding?.root
    }

    fun setupSpannableText() {
        val sourceString = requireContext().resources.getString(R.string.ocl_register_title)
        val spannable = SpannableString(sourceString)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    OclTracker.sendClickOnButtonDaftarEvent()
                    (activity as OclChooseAccountActivity).goToRegisterInitial()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(
                        activity,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                }
            },
            sourceString.indexOf(DAFTAR_INDEX),
            sourceString.length,
            0
        )
        binding?.oclBtnRegister?.movementMethod = LinkMovementMethod.getInstance()
        binding?.oclBtnRegister?.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpannableText()
        viewModel.mainLoader.observe(viewLifecycleOwner) {
            if(it) {
                binding?.mainView?.hide()
                binding?.chooseAccountLoader?.show()
            } else {
                binding?.mainView?.show()
                binding?.chooseAccountLoader?.hide()
            }
        }

        viewModel.toasterError.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                Toaster.build(view, it, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
            }
        }

        viewModel.navigateToNormalLogin.observe(viewLifecycleOwner) {
            val intent = RouteManager.getIntent(requireContext(), ApplinkConst.LOGIN)
            intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_FROM_OCL_LOGIN, true)
            startActivity(intent)
            activity?.finish()
        }

        viewModel.oclAccounts.observe(viewLifecycleOwner) {
            adapter?.setList(it)
        }

        viewModel.navigateToSuccessPage.observe(viewLifecycleOwner) {
            OclTracker.sendClickOnOneClickLoginEvent(OclTracker.LABEL_SUCCESS)
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }

        viewModel.loginFailedToaster.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                OclTracker.sendClickOnOneClickLoginEvent("${OclTracker.LABEL_FAILED} - $it")
                Toaster.build(view, it, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
            }
        }

        binding?.oclBtnOtherAcc?.setOnClickListener {
            OclTracker.sendClickOnMasukKeAkunLainEvent()
            val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
            intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_FROM_OCL_LOGIN, true)
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            startActivity(intent)
            activity?.finish()
        }

        viewModel.getOclAccounts()
    }

    override fun onAccountSelected(account: OclAccount) {
        OclTracker.sendClickOnOneClickLoginEvent(OclTracker.LABEL_CLICK)
        viewModel.loginOcl(account.token)
    }

    override fun onDeleteButtonClicked(account: OclAccount) {
        OclTracker.sendClickOnButtonHapusEvent()
        showDeleteConfirmationDialog(account)
    }

    private fun showDeleteConfirmationDialog(account: OclAccount) {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.ocl_delete_confirmation_title))
            dialog.setDescription(getString(R.string.ocl_delete_confirmation_subtitle))
            dialog.setPrimaryCTAText(getString(R.string.ocl_delete_confirmation_negative_btn_title))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                viewModel.deleteAccount(account)
                OclTracker.sendClickOnButtonHapusDialogEvent()
            }
            dialog.setSecondaryCTAText(getString(R.string.ocl_delete_confirmation_positive_btn_title))
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
                OclTracker.sendClickOnButtonBatalEvent()
            }
            dialog.show()
        }
    }

    companion object {
        const val DAFTAR_INDEX = "Daftar"
        fun createInstance(): Fragment {
            return OclChooseAccountFragment()
        }
    }
}
