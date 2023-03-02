package com.tokopedia.play.broadcaster.view.fragment.facefilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.broadcaster.databinding.FragmentFaceFilterSetupBinding
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroFaceFilterSetupBottomSheet
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 01, 2023
 */
class FaceFilterSetupFragment @Inject constructor(
    private val viewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
) : TkpdBaseV4Fragment() {

    override fun getScreenName(): String {
        return "FaceFilterSetupFragment"
    }

    private var _binding: FragmentFaceFilterSetupBinding? = null
    private val binding: FragmentFaceFilterSetupBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openFaceFilterBottomSheet()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFaceFilterSetupBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openFaceFilterBottomSheet() {
        PlayBroFaceFilterSetupBottomSheet.getFragment(
            childFragmentManager,
            requireContext().classLoader,
        ).show(childFragmentManager)
    }
}
