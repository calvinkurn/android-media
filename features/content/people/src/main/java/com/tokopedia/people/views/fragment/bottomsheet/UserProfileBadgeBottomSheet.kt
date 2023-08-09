package com.tokopedia.people.views.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.people.databinding.UpBottomSheetBadgeBinding
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by meyta.taliti on 30/07/23.
 */
class UserProfileBadgeBottomSheet : BottomSheetUnify() {

    private var _binding: UpBottomSheetBadgeBinding? = null
    private val binding: UpBottomSheetBadgeBinding
        get() = _binding!!

    private var mDataSource: DataSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = UpBottomSheetBadgeBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        setChild(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {
        showCloseIcon = true

        val badges = mDataSource?.badges() ?: return
        val badge = badges.first()

        binding.userProfileBadgeIcon.loadImage(badge.url)
        if (badge.detail != null) {
            setTitle(badge.detail.title)
            binding.userProfileBadgeTextDescription.text = badge.detail.desc
        }
    }

    fun setDataSource(dataSource: DataSource) {
        mDataSource = dataSource
    }

    fun show(manager: FragmentManager) {
        if (isVisible) return
        show(manager, TAG)
    }

    companion object {
        private const val TAG = "UserProfileBadgeBottomSheet"

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): UserProfileBadgeBottomSheet {
            return fragmentManager.findFragmentByTag(TAG) as? UserProfileBadgeBottomSheet ?: run {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    UserProfileBadgeBottomSheet::class.java.name
                ) as UserProfileBadgeBottomSheet
            }
        }
    }

    interface DataSource {
        fun badges(): List<ProfileUiModel.Badge>
    }
}
