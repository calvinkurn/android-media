package com.tokopedia.scp_rewards.cabinet.presentation.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.scp_rewards.R
import com.tokopedia.scp_rewards.cabinet.di.MedalCabinetComponent
import com.tokopedia.scp_rewards.cabinet.presentation.viewmodel.MedalCabinetViewModel
import com.tokopedia.scp_rewards.common.constants.TrackerConstants
import com.tokopedia.scp_rewards.databinding.FragmentMedalCabinetLayoutBinding
import javax.inject.Inject

class MedalCabinetFragment : BaseDaggerFragment() {

    private var windowInsetsController: WindowInsetsControllerCompat? = null
    private lateinit var binding: FragmentMedalCabinetLayoutBinding

    @Inject
    @JvmField
    var viewModelFactory: ViewModelFactory? = null

    private var medaliSlug = ""
    private var sourceName = ""

    private val medalCabinetViewModel by lazy {
        ViewModelProvider(this, viewModelFactory!!)[MedalCabinetViewModel::class.java]
    }

    override fun initInjector() {
        getComponent(MedalCabinetComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.intent?.let {
            medaliSlug = it.data?.pathSegments?.last() ?: ""
            sourceName = it.extras?.getString(
                ApplinkConstInternalPromo.SOURCE_PARAM,
                TrackerConstants.General.SOURCE_OTHER_PAGE
            ) ?: TrackerConstants.General.SOURCE_OTHER_PAGE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedalCabinetLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(binding.toolbar)
        setTopBottomMargin()
        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {

    }


    private fun setupToolbar(toolbar: androidx.appcompat.widget.Toolbar) {
        (activity as AppCompatActivity?)?.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                elevation = 0f
            }
            windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            setTransparentStatusBar()
        }
    }

    private fun setWhiteStatusBar() {
        (activity as AppCompatActivity?)?.apply {
            window?.statusBarColor = Color.WHITE
            binding.toolbar.setBackgroundColor(Color.WHITE)
            setToolbarBackButtonTint(R.color.Unify_NN900)

            windowInsetsController?.isAppearanceLightStatusBars = true
        }
    }

    private fun setTransparentStatusBar() {
        (activity as AppCompatActivity?)?.apply {
            activity?.window?.statusBarColor = Color.TRANSPARENT
            binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
            setToolbarBackButtonTint(R.color.Unify_NN0)

            windowInsetsController?.isAppearanceLightStatusBars = false
        }
    }

    private fun setTopBottomMargin() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }
            binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = insets.bottom
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setToolbarBackButtonTint(color: Int) {
        context?.let {
            binding.toolbar.navigationIcon?.setTint(
                ContextCompat.getColor(it, color)
            )
        }
    }

    override fun getScreenName() = ""
}
