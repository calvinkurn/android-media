package com.tokopedia.deals.pdp.ui.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.databinding.FragmentDealsDetailDescBinding
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.header.HeaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable

class DealsPDPDescFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentDealsDetailDescBinding>()
    private var toolbar: HeaderUnify? = null
    private var tgExpendableDesc: Typography? = null
    private var text = ""
    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        title = arguments?.getString(EXTRA_TITLE, "") ?: ""
        text = arguments?.getString(EXTRA_TEXT, "") ?: ""
        super.onCreate(savedInstanceState)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DealsPDPComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDealsDetailDescBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        view?.apply {
            toolbar = binding?.toolbar
            tgExpendableDesc = binding?.tgExpandableDescription

            toolbar?.headerTitle = title
            tgExpendableDesc?.text = Html.fromHtml(DealsUtils.getExpandableItemText(text))

            (activity as DealsPDPActivity).setSupportActionBar(toolbar)
            (activity as DealsPDPActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    companion object {
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_TEXT = "EXTRA_TEXT"

        fun createInstance(title: String, text: String): DealsPDPDescFragment {
            val fragment = DealsPDPDescFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_TITLE, title)
            bundle.putString(EXTRA_TEXT, text)
            fragment.arguments = bundle
            return fragment
        }
    }
}
