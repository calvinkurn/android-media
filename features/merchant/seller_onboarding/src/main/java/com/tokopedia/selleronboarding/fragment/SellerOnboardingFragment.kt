package com.tokopedia.selleronboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.selleronboarding.R
import kotlinx.android.synthetic.main.fragment_sob_onboarding.view.*

/**
 * Created By @ilhamsuaib on 09/04/20
 */

class SellerOnboardingFragment : Fragment() {

    companion object {

        private const val KEY_PAGE = "page"
        const val PAGE_1 = 1
        const val PAGE_2 = 2
        const val PAGE_3 = 3

        fun newInstance(page: Int): SellerOnboardingFragment {
            return SellerOnboardingFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_PAGE, page)
                }
            }
        }
    }

    private val onboardingPage by lazy {
        arguments?.getInt(KEY_PAGE, PAGE_1).orZero()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sob_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnboarding()
    }

    private fun setupOnboarding() {
        when(onboardingPage) {
            PAGE_1 -> {
                val headerText = getString(R.string.sob_header_text_page_1)
                val drawableRes = R.drawable.sob_illustration_onboarding_1
                showOnboardingPage(headerText, drawableRes)
            }
            PAGE_2 -> {
                val headerText = getString(R.string.sob_header_text_page_2)
                val drawableRes = R.drawable.sob_illustration_onboarding_2
                showOnboardingPage(headerText, drawableRes)
            }
            PAGE_3 -> {
                val headerText = getString(R.string.sob_header_text_page_3)
                val drawableRes = R.drawable.sob_illustration_onboarding_3
                showOnboardingPage(headerText, drawableRes)
            }
        }
    }

    private fun showOnboardingPage(headerText: String, @DrawableRes drawableRes: Int) {
        view?.run {
            tvHeaderText.text = headerText
            imgIllustrationSob.loadImageDrawable(drawableRes)
        }
    }
}