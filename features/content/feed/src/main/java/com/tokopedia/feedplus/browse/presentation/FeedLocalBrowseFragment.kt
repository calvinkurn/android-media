package com.tokopedia.feedplus.browse.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.feedplus.databinding.FragmentFeedLocalBrowseBinding
import com.tokopedia.feedplus.R as feedplusR

class FeedLocalBrowseFragment: BaseDaggerFragment() {

    private var binding: FragmentFeedLocalBrowseBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedLocalBrowseBinding.inflate(inflater)

        setupHeader()

        return binding?.root
    }

    override fun getScreenName(): String {
        return "Browse Local"
    }

    override fun initInjector() {}

    private fun setupHeader() {
        binding?.feedHeader?.let {
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
        const val LOCAL_BROWSE_SEARCH_KEYWORD = "feed_local_browse_keyword"

        fun create(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle?
        ): FeedLocalBrowseFragment {
            return fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FeedLocalBrowseFragment::class.java.name
            ).apply {
                arguments = bundle
            } as FeedLocalBrowseFragment
        }
    }
}
