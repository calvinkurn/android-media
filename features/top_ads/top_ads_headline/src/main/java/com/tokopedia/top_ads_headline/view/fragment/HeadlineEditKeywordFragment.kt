package com.tokopedia.top_ads_headline.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.di.HeadlineAdsComponent
import kotlinx.android.synthetic.main.fragment_headline_edit_keyword.*
import kotlinx.android.synthetic.main.topads_headline_keyword_empty_state.*

const val KEYWORD_POSITIVE = "keywordPositive"
const val KEYWORD_NEGATIVE = "keywordNegative"
const val KEYWORD_TYPE = "keywordType"

class HeadlineEditKeywordFragment : BaseDaggerFragment() {
    private var keywordType: String = ""
    private var keywordList: ArrayList<String> = ArrayList()

    companion object {
        fun getInstance(keywordType: String): HeadlineEditKeywordFragment {
            return HeadlineEditKeywordFragment().apply {
                arguments = Bundle().apply {
                    putString(KEYWORD_TYPE, keywordType)
                }
            }
        }
    }

    override fun getScreenName(): String {
        return HeadlineEditKeywordFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(HeadlineAdsComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            keywordType = getString(KEYWORD_TYPE) ?: ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.fragment_headline_edit_keyword), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (keywordList.isEmpty()) {
            keyword_counter.hide()
            add_keyword.hide()
            keyword_list.hide()
            emptyStateIcon.show()
            title.show()
            subTitle.show()
            ctaBtn.show()
            if (keywordType == KEYWORD_POSITIVE) {
                title.text = getString(R.string.topads_headline_edit_keyword_empty_kata_kunci_header)
                subTitle.text = getString(R.string.topads_headline_edit_keyword_empty_kata_kunci_subheader)
                ctaBtn.text = getString(R.string.topads_headline_edit_keyword_empty_kata_kunci_cta)
            } else {
                title.text = getString(R.string.topads_headline_edit_keyword_empty_kata_kunci_negatif_header)
                subTitle.text = getString(R.string.topads_headline_edit_keyword_empty_kata_kunci_negatif_subheader)
                ctaBtn.text = getString(R.string.topads_headline_edit_keyword_empty_kata_kunci_negatif_cta)
            }
        } else {
            emptyStateIcon.hide()
            title.hide()
            subTitle.hide()
            ctaBtn.hide()
            keyword_counter.show()
            add_keyword.show()
            keyword_list.hide()
        }
    }
}