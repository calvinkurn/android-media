package com.tokopedia.feedplus.browse.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.feedplus.databinding.FragmentFeedLocalSearchBinding
import com.tokopedia.feedplus.R as feedplusR

class FeedLocalSearchFragment : TkpdBaseV4Fragment() {

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
            it.setSearchPlaceholder(getString(feedplusR.string.feed_local_search_page_text_placeholder))
            it.setSearchDoneListener { keyword ->
                submitSearchKeyword(keyword)
            }
            it.setSearchFocus()
        }
    }

    private fun onBackPressed() {
        activity?.finish()
    }

    private fun submitSearchKeyword(keyword: String) {
        val intent = Intent()
        intent.putExtra(LOCAL_BROWSE_SEARCH_KEYWORD, keyword)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    companion object {
        const val LOCAL_BROWSE_SEARCH_KEYWORD = "feed_local_search_keyword"

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
