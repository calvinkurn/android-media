package com.tokopedia.stories.creation.view.stateholder

import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.tokopedia.nest.components.rememberNestBottomSheetState
import com.tokopedia.stories.creation.di.StoriesCreationScope
import com.tokopedia.stories.creation.view.model.StoriesCreationBottomSheetType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 07, 2023
 */
@OptIn(ExperimentalMaterialApi::class)
@StoriesCreationScope
class StoriesCreationBottomSheetStateHolder @Inject constructor() {

    private lateinit var _scope: CoroutineScope
    private lateinit var _bottomSheetType: MutableState<StoriesCreationBottomSheetType>
    private lateinit var _sheetState: BottomSheetScaffoldState

    val bottomSheetType: StoriesCreationBottomSheetType
        get() = _bottomSheetType.value

    val sheetState: BottomSheetScaffoldState
        get() = _sheetState

    @Composable
    fun initState() {
        _sheetState = rememberNestBottomSheetState()
        _scope = rememberCoroutineScope()
        _bottomSheetType = remember {
            mutableStateOf(StoriesCreationBottomSheetType.Unknown)
        }
    }

    fun showBottomSheet(bottomSheetType: StoriesCreationBottomSheetType) {
        _scope.launch {
            _bottomSheetType.value = bottomSheetType
            _sheetState.bottomSheetState.expand()
        }
    }

    fun dismissBottomSheet() {
        _scope.launch {
            _sheetState.bottomSheetState.collapse()
        }
    }
}
