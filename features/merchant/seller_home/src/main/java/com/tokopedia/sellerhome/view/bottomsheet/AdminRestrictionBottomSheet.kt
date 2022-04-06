package com.tokopedia.sellerhome.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.sellerhome.R
import com.tokopedia.shop.common.constant.admin_roles.AdminPermissionUrl
import com.tokopedia.unifycomponents.BottomSheetUnify

class AdminRestrictionBottomSheet: BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context): AdminRestrictionBottomSheet =
            AdminRestrictionBottomSheet().apply {
                val view = View.inflate(
                    context,
                    R.layout.bottom_sheet_sah_admin_restriction,
                    null
                )
                setChild(view)
            }

        private const val TAG = "AdminRestrictionBottomSheet"
    }

    private var adminRestrictionEmptyState: EmptyStateUnify? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupView(view: View) {
        adminRestrictionEmptyState = view.findViewById(R.id.empty_state_sah_admin_restriction)
        adminRestrictionEmptyState?.run {
            setImageUrl(AdminPermissionUrl.ERROR_ILLUSTRATION)
            setPrimaryCTAClickListener {
                this@AdminRestrictionBottomSheet.dismiss()
            }
        }
    }

}