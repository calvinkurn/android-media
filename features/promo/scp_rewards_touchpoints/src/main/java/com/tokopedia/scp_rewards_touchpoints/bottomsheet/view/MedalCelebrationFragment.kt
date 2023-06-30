package com.tokopedia.scp_rewards_touchpoints.bottomsheet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.scp_rewards_touchpoints.common.di.CelebrationComponent
import com.tokopedia.scp_rewards_touchpoints.common.Loading
import com.tokopedia.scp_rewards_touchpoints.common.Success
import com.tokopedia.scp_rewards_touchpoints.common.Error
import com.tokopedia.scp_rewards_touchpoints.databinding.ActivityTestBinding
import com.tokopedia.scp_rewards_touchpoints.toaster.model.ScpRewardsToasterModel
import com.tokopedia.scp_rewards_touchpoints.toaster.viewmodel.ScpToasterViewModel
import com.tokopedia.scp_rewards_touchpoints.view.toaster.ScpRewardsToaster
import kotlinx.android.synthetic.main.celebration_main_layout.view.*
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MedalCelebrationFragment : BaseDaggerFragment() {

    private var medaliSlug = "UNILEVER_CLUB"
    private var binding: ActivityTestBinding? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val scpToasterViewModel: ScpToasterViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider[ScpToasterViewModel::class.java]
    }

    override fun initInjector() {
        getComponent(CelebrationComponent::class.java).inject(this@MedalCelebrationFragment)
    }

    override fun getScreenName() = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        activity?.intent?.let {
//            medaliSlug = it.data?.pathSegments?.last() ?: "UNILEVER_CLUB"
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityTestBinding.inflate(inflater, container, false)

        return binding?.root?.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
//        binding?.btnBs?.setOnClickListener {
//            MedalCelebrationBottomSheet.show(childFragmentManager, "UNILEVER_CLUB")
//        }

        binding?.btnToaster?.setOnClickListener {
            scpToasterViewModel.getToaster(914957011, "", "order_history_list_page")
        }

    }
    private fun observeData() {
        scpToasterViewModel.toasterLiveData.observe(this) {
            when (it) {
                is Success<*> -> {
                    val data = (it.data as ScpRewardsToasterModel).scpRewardsMedaliTouchpointOrder
                    if (data?.isShown == true) {
                        val title = data.medaliTouchpointOrder?.infoMessage?.title?: ""
                        val subtitle = data.medaliTouchpointOrder?.infoMessage?.subtitle?: ""
                        val ctaTitle = data.medaliTouchpointOrder?.cta?.text ?: ""
                        val appLink = data.medaliTouchpointOrder?.cta?.appLink

                        view?.let { it1 ->
                            ScpRewardsToaster.build(it1, title, subtitle, 4000, actionText = ctaTitle, clickListener = {
                                RouteManager.route(context, appLink)
                            }).show()
                        }
                    }
                }

                is Error -> {

                }
                Loading -> {

                }
            }
        }
    }
}
