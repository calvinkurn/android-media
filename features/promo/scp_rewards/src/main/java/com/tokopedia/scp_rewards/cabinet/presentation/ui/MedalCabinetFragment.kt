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
import androidx.core.view.postDelayed
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.scp_rewards.R
import com.tokopedia.scp_rewards.cabinet.di.MedalCabinetComponent
import com.tokopedia.scp_rewards.cabinet.domain.model.MedaliCabinetData
import com.tokopedia.scp_rewards.cabinet.presentation.viewmodel.MedalCabinetViewModel
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.databinding.FragmentMedalCabinetLayoutBinding
import com.tokopedia.scp_rewards_common.EARNED_BADGE
import com.tokopedia.scp_rewards_widgets.cabinetHeader.CabinetHeader
import com.tokopedia.scp_rewards_widgets.medal.MedalClickListener
import com.tokopedia.scp_rewards_widgets.medal.MedalData
import com.tokopedia.scp_rewards_widgets.medal.MedalItem
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class MedalCabinetFragment : BaseDaggerFragment() {

    private var windowInsetsController: WindowInsetsControllerCompat? = null
    private lateinit var binding: FragmentMedalCabinetLayoutBinding

    @Inject
    @JvmField
    var viewModelFactory: ViewModelFactory? = null

    private var medaliSlug = ""

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory!!)[MedalCabinetViewModel::class.java]
    }

    override fun initInjector() {
        getComponent(MedalCabinetComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.intent?.let {
            medaliSlug = it.extras?.getString(
                ApplinkConstInternalPromo.SOURCE_PARAM,
                ""
            ) ?: ""
        }
        activity?.window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
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
        viewModel.getHomePage(medaliSlug)
        setupToolbar(binding.toolbar)
        setTopBottomMargin()
        setupScrollListener()
        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {
        viewModel.cabinetLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success<*> -> {
                    showData(it.data as MedaliCabinetData)
                }

                is Loading -> {}
                is Error -> {}
                else -> {}
            }
        }
    }

    private fun showData(medalCabinetData: MedaliCabinetData) {
        binding.viewCabinet.postDelayed(1500) {
            binding.mainFlipper.displayedChild = 1
            binding.viewCabinet.bindData(
                cabinetHeader = medalCabinetData.header ?: CabinetHeader(),
                medalList = listOf(
                    medalCabinetData.earnedMedaliData ?: MedalData(),
                    medalCabinetData.progressMedaliData ?: MedalData()
                )
            )
            binding.viewCabinet.attachMedalClickListener(object : MedalClickListener {
                override fun onMedalClick(medalItem: MedalItem) {
                }

                override fun onSeeMoreClick(medalData: MedalData) {
                }
            })
        }
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

    private fun setupScrollListener() {
        binding.viewCabinet.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (binding.viewCabinet.computeVerticalScrollOffset() == 0) {
                    setTransparentStatusBar()
                } else {
                    setWhiteStatusBar()
                }
            }
        })
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

    private fun handleError(error: Throwable) {
        setWhiteStatusBar()
        setToolbarBackButtonTint(R.color.Unify_NN900)
        binding.loadContainer.loaderFlipper.displayedChild = 1
        if (error is UnknownHostException || error is SocketTimeoutException) {
            binding.loadContainer.medalCabinetError.setType(GlobalError.NO_CONNECTION)
        } else {
            binding.loadContainer.medalCabinetError.apply {
                setType(GlobalError.SERVER_ERROR)
                errorSecondaryAction.text = context.getText(R.string.goto_main_page_text)
            }
        }
    }

    override fun getScreenName() = ""
}
