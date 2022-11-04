package com.tokopedia.privacycenter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.privacyacenter.databinding.FragmentPrivacyCenterBinding
import com.tokopedia.privacycenter.di.PrivacyCenterComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PrivacyCenterFragment : BaseDaggerFragment(), AppBarLayout.OnOffsetChangedListener {

    private var binding by autoClearedNullable<FragmentPrivacyCenterBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            PrivacyCenterViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrivacyCenterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        binding?.appbar?.addOnOffsetChangedListener(this)
    }

    private fun initToolbar() {
        binding?.unifyToolbar?.apply {
            title = "Pengaturan Privasi"
            setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        binding?.appbar?.removeOnOffsetChangedListener(this)
    }

    override fun getScreenName(): String {
        return PrivacyCenterFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PrivacyCenterComponent::class.java).inject(this)
    }

    companion object {
        fun newInstance() = PrivacyCenterFragment()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (verticalOffset == 0) {
            val textColor = ContextCompat.getColor(requireActivity(), android.R.color.white)
            val backIconWhite = getIconUnifyDrawable(requireActivity(), IconUnify.ARROW_BACK, ContextCompat.getColor(requireActivity(), android.R.color.white))
            binding?.unifyToolbar?.headerView?.setTextColor(textColor)
            binding?.unifyToolbar?.navigationIcon = backIconWhite
            requireActivity().window.statusBarColor = Color.TRANSPARENT

            setTextStatusBar(true)
        } else {
            val textColor = getTextColor(getWhite = !viewModel.isUsingDarkMode)
            val backIconWhite = getIconBackWithColor(getWhite = !viewModel.isUsingDarkMode)
            binding?.unifyToolbar?.headerView?.setTextColor(textColor)
            binding?.unifyToolbar?.navigationIcon = backIconWhite
            requireActivity().window.statusBarColor = MethodChecker.getColor(requireActivity(), com.tokopedia.unifyprinciples.R.color.Unify_Background)

            setTextStatusBar(setToWhite = !viewModel.isUsingDarkMode)
        }
    }

    private fun setTextStatusBar(setToWhite: Boolean) {
        requireActivity().window.decorView.systemUiVisibility =
            if (setToWhite)
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            else
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    //true for white, false for black
    private fun getIconBackWithColor(getWhite: Boolean): Drawable? {
        return getIconUnifyDrawable(
            requireActivity(),
            IconUnify.ARROW_BACK,
            ContextCompat.getColor(
                requireActivity(),
                getColor(getWhite)
            )
        )
    }

    //true for white, false for black
    private fun getTextColor(getWhite: Boolean): Int {
        return ContextCompat.getColor(
            requireActivity(),
            getColor(getWhite)
        )
    }

    //true for white, false for black
    private fun getColor(getWhite: Boolean): Int {
        return if (getWhite)
            android.R.color.white
        else
            android.R.color.black
    }
}
