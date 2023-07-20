package com.tokopedia.scp_rewards_touchpoints.bottomsheet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.scp_rewards_touchpoints.common.Error
import com.tokopedia.scp_rewards_touchpoints.common.Loading
import com.tokopedia.scp_rewards_touchpoints.common.Success
import com.tokopedia.scp_rewards_touchpoints.common.di.CelebrationComponent
import com.tokopedia.scp_rewards_touchpoints.databinding.ActivityTestBinding
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.response.ScpRewardsMedalTouchPointResponse
import com.tokopedia.scp_rewards_touchpoints.touchpoints.viewmodel.ScpRewardsMedalTouchPointViewModel
import com.tokopedia.scp_rewards_touchpoints.touchpoints.ScpToasterHelper
import javax.inject.Inject


class MedalCelebrationFragment : BaseDaggerFragment() {

    private var medaliSlug = "UNILEVER_CLUB"
    private var binding: ActivityTestBinding? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val scpMedalTouchPointViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider[ScpRewardsMedalTouchPointViewModel::class.java]
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
            if (binding?.input?.editText?.text?.isEmpty() == true) {
                Toast.makeText(context, "Please enter order id", Toast.LENGTH_LONG).show()
            } else {
                scpMedalTouchPointViewModel.getMedalTouchPoint(binding?.input?.editText?.text?.toString()?.toLong() ?: 0, "", "order_history_list_page")
            }
        }
    }
    private fun observeData() {
        scpMedalTouchPointViewModel.medalTouchPointData.observe(this) {
            when (it) {
                is Success<*> -> {
                    binding?.btnToaster?.isLoading = false
                    val data = (it.data as ScpRewardsMedalTouchPointResponse)
                    if (data.scpRewardsMedaliTouchpointOrder.isShown) {
                        view?.let { v ->
                            ScpToasterHelper.showToaster(
                                view = v,
                                data = data
                            )
                        }
                    }
                }
                is Error -> {
                    binding?.btnToaster?.isLoading = false
                    Toast.makeText(context, "User doesnt have medalis", Toast.LENGTH_LONG).show()
                }
                Loading -> {
                    binding?.btnToaster?.isLoading = true
                }
            }
        }
    }
}
