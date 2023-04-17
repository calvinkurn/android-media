package com.tokopedia.chooseaccount.view.ocl

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.chooseaccount.data.OclAccount
import com.tokopedia.chooseaccount.databinding.FragmentOclChooseAccountBinding
import com.tokopedia.chooseaccount.di.ChooseAccountComponent
import com.tokopedia.chooseaccount.view.adapter.OclAccountAdapter
import com.tokopedia.chooseaccount.view.listener.OclChooseAccountListener
import com.tokopedia.chooseaccount.viewmodel.OclChooseAccountViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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
        binding?.oclChooseAccountList?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.oclChooseAccountList?.adapter = adapter
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.mainLoader.observe(this) {
            if(it) {
                binding?.mainView?.hide()
                binding?.chooseAccountLoader?.show()
            } else {
                binding?.mainView?.show()
                binding?.chooseAccountLoader?.hide()
            }
        }

        viewModel.toasterError.observe(this) {
            if(it.isNotEmpty()) {
                Toaster.build(view, it, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
            }
        }

        viewModel.navigateToNormalLogin.observe(this) {
            if(it) {
                val intent = RouteManager.getIntent(requireContext(), ApplinkConst.LOGIN)
                startActivity(intent)
                activity?.finish()
            }
        }

        viewModel.oclAccounts.observe(this) {
            adapter?.setList(it)
        }

        viewModel.getOclAccounts()
    }

    override fun onAccountSelected(account: OclAccount) {
        val intent = Intent().apply {
            putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, account.token)
        }
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onDeleteButtonClicked(account: OclAccount) {

    }

    companion object {
        fun createInstance(): Fragment {
            return OclChooseAccountFragment()
        }
    }
}
