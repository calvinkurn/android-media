package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.BottomSheetProductArchivalBinding
import com.tokopedia.product.manage.feature.list.data.model.ProductArchivalInfo
import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.di.ProductManageListInstance
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject


class ProductArchivalBottomSheet : BottomSheetUnify(), HasComponent<ProductManageListComponent> {

    private var onErrorArchivalInfoListener: ((String) -> Unit)? = null

    companion object {
        private val TAG: String = ProductArchivalBottomSheet::class.java.simpleName
        private const val PRODUCT_ID_KEY = "product_id"
        private const val IS_ARCHIVED_KEY = "is_archived"
        private const val IS_GRACE_PERIOD_KEY = "is_grace_period"

        fun createInstance(
            productId: String,
            isArchived: Boolean,
            isGracePeriod: Boolean,
            listener: ((String) -> Unit)?
        ): ProductArchivalBottomSheet {
            return ProductArchivalBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PRODUCT_ID_KEY, productId)
                    putBoolean(IS_ARCHIVED_KEY, isArchived)
                    putBoolean(IS_GRACE_PERIOD_KEY, isGracePeriod)

                }
                onErrorArchivalInfoListener = listener
            }
        }
    }

    @Inject
    lateinit var viewModel: ProductManageViewModel

    private val productId by lazy {
        arguments?.getString(PRODUCT_ID_KEY).orEmpty()
    }

    private val isArchived by lazy {
        arguments?.getBoolean(IS_ARCHIVED_KEY).orFalse()
    }

    private val isGracePeriod by lazy {
        arguments?.getBoolean(IS_GRACE_PERIOD_KEY).orFalse()
    }
    private var binding by autoClearedNullable<BottomSheetProductArchivalBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProductArchivalBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component?.inject(this)
        super.onCreate(savedInstanceState)
        setStyle(
            DialogFragment.STYLE_NORMAL,
            com.tokopedia.product.manage.common.R.style.DialogStyle
        )
    }

    override fun getComponent(): ProductManageListComponent? {
        return activity?.run {
            ProductManageListInstance.getComponent(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeProductArchival()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()
                ?.remove(this@ProductArchivalBottomSheet)?.commit()
        }
    }

    fun show(
        fm: FragmentManager,
    ) {
        show(fm, TAG)
    }

    private fun setupView() {
        val title =
            context?.getString(R.string.product_manage_text_title_product_archival_bottom_sheet)
                .orEmpty()
        setTitle(title)
    }

    private fun observeProductArchival() {
        viewModel.productArchivalInfo.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    setupContent(result.data.productarchivalGetProductArchiveInfo)
                }

                is Fail -> {
                    dismiss()
                    onErrorArchivalInfoListener?.invoke(result.throwable.message.orEmpty())
                }
            }
        }
        viewModel.getProductArchivalInfo(productId)
    }

    private fun setupContent(content: ProductArchivalInfo.ProductarchivalGetProductArchiveInfo) {

        binding?.apply {
            shimmerGroup.gone()
            productArchivalGroup.show()
            tvProductArchivalTime.text = if (isGracePeriod){
                getString(R.string.product_archival_info_time_grace_period, content.archiveTime).parseAsHtml()
            }else{
                getString(R.string.product_archival_info_time_archived, content.archiveTime).parseAsHtml()
            }


            tvProductArchivalReason.text = content.reason
            tvProductArchivalSellerEdu.apply{
                val htmlText = context?.let {
                    HtmlLinkHelper(
                        it,
                        getString(R.string.product_archival_edu, content.sellerEduArticleURL)
                    )
                }
                this.movementMethod = LinkMovementMethod.getInstance()
                this.highlightColor = Color.TRANSPARENT
                htmlText?.urlList?.getOrNull(Int.ZERO)?.setOnClickListener {
                    RouteManager.route(requireContext(),content.sellerEduArticleURL)
                }
                this.text = htmlText?.spannedString
            }

            btnProductArchivalHelp.setOnClickListener {
                RouteManager.route(requireContext(), content.helpPageURL)
            }
        }
    }
}
