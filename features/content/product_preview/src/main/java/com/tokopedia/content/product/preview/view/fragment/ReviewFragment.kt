package com.tokopedia.content.product.preview.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.UriUtil
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.product.preview.databinding.FragmentReviewBinding
import com.tokopedia.content.product.preview.utils.PAGE_SOURCE
import com.tokopedia.content.product.preview.utils.REVIEW_CREDIBILITY_APPLINK
import com.tokopedia.content.product.preview.utils.REVIEW_FRAGMENT_TAG
import com.tokopedia.content.product.preview.view.adapter.review.ReviewParentAdapter
import com.tokopedia.content.product.preview.view.uimodel.AuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewParentContentViewHolder
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReviewFragment @Inject constructor(
    private val router: Router
) : TkpdBaseV4Fragment(), ReviewParentContentViewHolder.Listener {

    private var _binding: FragmentReviewBinding? = null
    private val binding: FragmentReviewBinding
        get() = _binding!!

    private val parentPage: ProductPreviewFragment
        get() = (requireParentFragment() as ProductPreviewFragment)

    private val viewModel by activityViewModels<ProductPreviewViewModel> {
        parentPage.viewModelProvider
    }

    private val reviewAdapter by lazyThreadSafetyNone {
        ReviewParentAdapter(this)
    }

    private val snapHelper = PagerSnapHelper() // TODO: adjust pager snap helper

    override fun getScreenName() = REVIEW_FRAGMENT_TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeReview()
    }

    override fun onResume() {
        super.onResume()

        viewModel.onAction(ProductPreviewAction.FetchReview)
    }

    private fun setupView() {
        binding.rvReview.adapter = reviewAdapter
        snapHelper.attachToRecyclerView(binding.rvReview)
    }

    private fun observeReview() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.review.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.RESUMED
            ).withCache().collectLatest { (prev, curr) ->
                renderList(prev, curr)
            }
        }
    }

    private fun renderList(prev: List<ReviewUiModel>?, data: List<ReviewUiModel>) {
        if (prev == data) return // TODO: adjust condition
        reviewAdapter.submitList(data)
    }

    /**
     * Review Content Listener
     */
    override fun onReviewCredibilityClicked(author: AuthorUiModel) {
        val appLink = UriUtil.buildUri(REVIEW_CREDIBILITY_APPLINK, author.id, PAGE_SOURCE)
        router.route(requireContext(), appLink)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): ReviewFragment {
            val oldInstance = fragmentManager.findFragmentByTag(REVIEW_FRAGMENT_TAG) as? ReviewFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ReviewFragment::class.java.name
            ).apply {
                arguments = bundle
            } as ReviewFragment
        }
    }
}
