package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.play.broadcaster.R

/**
 * Created by Jonathan Darwin on 01 March 2024
 */
class EstimatedIncomeInfoBottomSheet : BottomSheetUnify() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        setTitle(getString(R.string.play_broadcaster_live_stats_estimated_income_label))

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            
            setContent {
                NestTheme(
                    isOverrideStatusBarColor = false
                ) {
                    Surface(
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        NestTypography(
                            text = stringResource(id = R.string.play_broadcaster_estimated_income_info),
                            textStyle = NestTheme.typography.body2
                        )
                    }
                }
            }
        }

        setChild(composeView)
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "EstimatedIncomeInfoBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): EstimatedIncomeInfoBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? EstimatedIncomeInfoBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                EstimatedIncomeInfoBottomSheet::class.java.name
            ) as EstimatedIncomeInfoBottomSheet
        }
    }
}
