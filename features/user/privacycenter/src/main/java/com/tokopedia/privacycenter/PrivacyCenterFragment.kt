package com.tokopedia.privacycenter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.privacycenter.common.getDynamicColorStatusBar
import com.tokopedia.privacycenter.common.getIconBackWithColor
import com.tokopedia.privacycenter.common.getIdColor
import com.tokopedia.privacycenter.common.setFitToWindows
import com.tokopedia.privacycenter.common.setTextStatusBar
import com.tokopedia.privacycenter.databinding.FragmentPrivacyCenterBinding
import com.tokopedia.privacycenter.di.PrivacyCenterComponent
import com.tokopedia.unifycomponents.isUsingNightModeResources
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
    }

    override fun onStart() {
        super.onStart()
        binding?.appbar?.addOnOffsetChangedListener(this)
    }

    private fun initToolbar() {
        requireActivity().apply {
            setFitToWindows(false)
            window.statusBarColor = Color.TRANSPARENT
            binding?.unifyToolbar?.apply {
                title = resources.getString(R.string.title_privacy_center)
                setBackgroundColor(Color.TRANSPARENT)
                setNavigationOnClickListener {
                    onBackPressed()
                }
            }
        }
        binding?.textName?.text = viewModel.getUserName()
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
        setUpCollapseToolbar(verticalOffset < -136)
    }

    private fun setUpCollapseToolbar(isCollapsed: Boolean) {
        val getWhiteColor = if (isCollapsed) {
            isUsingNightModeResources()
        } else {
            true
        }

        requireActivity().apply {
            val textColor = getIdColor(getWhite = getWhiteColor)
            val backIcon = getIconBackWithColor(getWhite = getWhiteColor)

            window.statusBarColor = if (getWhiteColor) {
                Color.TRANSPARENT
            } else {
                getDynamicColorStatusBar()
            }
            setTextStatusBar(setToWhite = getWhiteColor)

            binding?.unifyToolbar?.apply {
                headerView?.setTextColor(textColor)
                navigationIcon = backIcon
            }
        }

    }
}
