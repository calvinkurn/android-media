package com.tokopedia.shopadmin.feature.invitationaccepted.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shopadmin.R
import com.tokopedia.shopadmin.common.utils.ShopAdminErrorLogger
import com.tokopedia.shopadmin.databinding.BottomsheetTncAdminBinding
import com.tokopedia.shopadmin.feature.invitationaccepted.di.component.AdminInvitationAcceptedComponent
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.ArticleDetailUiModel
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.viewmodel.InvitationAcceptedViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TncAdminBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding by autoClearedNullable<BottomsheetTncAdminBinding>()

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            InvitationAcceptedViewModel::class.java
        )
    }

    @SuppressWarnings("unchecked")
    private fun <C> getComponent(componentType: Class<C>): C? {
        return componentType.cast((activity as? HasComponent<C>)?.component)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFullpage = true
        getComponent(AdminInvitationAcceptedComponent::class.java)?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStop() {
        childFragmentManager.fragments.forEach {
            if (it is BottomSheetUnify) it.dismiss()
        }
        super.onStop()
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeArticleDetail()
        fetchArticleDetail()
    }

    private fun fetchArticleDetail() {
        showLoading()
        viewModel.fetchArticleDetail()
    }

    private fun observeArticleDetail() {
        observe(viewModel.articleDetail) {
            when (it) {
                is Success -> {
                    hideLoading()
                    setupViews(it.data)
                }
                is Fail -> {
                    showToaster()
                    ShopAdminErrorLogger.logToCrashlytic(
                        ShopAdminErrorLogger.ARTICLE_DETAIL_ERROR,
                        it.throwable
                    )
                }
            }
        }
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(R.layout.bottomsheet_tnc_admin, container, false)
        binding = BottomsheetTncAdminBinding.bind(view)
        setChild(view)
    }

    private fun setupViews(articleDetailUiModel: ArticleDetailUiModel) = binding?.run {
        tvTncSummaryContent.text = MethodChecker.fromHtml(articleDetailUiModel.htmlContent)
    }

    private fun showLoading() {
        if (binding?.loaderTncAdmin?.isVisible == false) {
            binding?.loaderTncAdmin?.show()
        }
    }

    private fun hideLoading() {
        if (binding?.loaderTncAdmin?.isVisible == true) {
            binding?.loaderTncAdmin?.hide()
        }
    }

    private fun showToaster() {
        view?.let {
            Toaster.build(
                it,
                text = getString(com.tokopedia.shopadmin.R.string.error_message_article_detail),
                type = Toaster.TYPE_ERROR,
                actionText = getString(com.tokopedia.shopadmin.R.string.error_admin_try_again_action),
                clickListener = {
                    fetchArticleDetail()
                }
            ).show()
        }
    }

    companion object {
        val TAG: String = TncAdminBottomSheet::class.java.simpleName
    }
}