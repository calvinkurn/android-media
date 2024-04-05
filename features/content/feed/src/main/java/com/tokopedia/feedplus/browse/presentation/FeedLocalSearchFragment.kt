package com.tokopedia.feedplus.browse.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.content.common.util.Router
import com.tokopedia.feedplus.databinding.FragmentFeedLocalSearchBinding
import javax.inject.Inject
import com.tokopedia.feedplus.R as feedplusR

class FeedLocalSearchFragment @Inject constructor(
    private val router: Router,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentFeedLocalSearchBinding? = null
    private val binding: FragmentFeedLocalSearchBinding get() = _binding!!

    override fun getScreenName(): String = "Feed Local Search"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedLocalSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupHeader() {
        binding.feedHeader.let {
            it.onBackClicked {
                onBackPressed()
            }

            val keyword = getStringExtra(FeedLocalSearchActivity.TAG_KEYWORD)
            it.setSearchbarText(keyword)

            val placeholder = getStringExtra(
                FeedLocalSearchActivity.TAG_PLACEHOLDER_PARAM,
                getString(feedplusR.string.feed_local_search_page_text_placeholder_fallback)
            )
            it.setSearchPlaceholder(placeholder)

            it.setSearchDoneListener { keyword ->
                submitSearchKeyword(keyword)
            }

            it.setSearchFocus()
        }
    }

    private fun onBackPressed() {
        activity?.finish()
    }

    private fun getStringExtra(key: String, default: String = ""): String {
        return activity?.intent?.getStringExtra(key) ?: default
    }

    private fun submitSearchKeyword(keyword: String) {
        if (keyword.isBlank()) return

        val intent = router.getIntent(context, ApplinkConstInternalContent.INTERNAL_FEED_SEARCH_RESULT).apply {
            putExtra(FeedSearchResultActivity.KEYWORD_PARAM, keyword)
        }
        router.route(requireActivity(), intent)

        activity?.finish()
    }

    companion object {
        fun create(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle?
        ): FeedLocalSearchFragment {
            return fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FeedLocalSearchFragment::class.java.name
            ).apply {
                arguments = bundle
            } as FeedLocalSearchFragment
        }
    }
}
