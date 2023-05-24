package com.tokopedia.mediauploader.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.common_compose.components.NestButton
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.mediauploader.DebugMediaLoaderEvent
import com.tokopedia.mediauploader.DebugMediaLoaderState
import com.tokopedia.mediauploader.DebugMediaUploaderViewModelContract
import com.tokopedia.mediauploader.DebugUploaderParam
import com.tokopedia.mediauploader.data.entity.LogType
import com.tokopedia.mediauploader.data.entity.Logs
import com.tokopedia.mediauploader.ui.component.BrowseFileButton
import com.tokopedia.mediauploader.ui.component.ConfigButton
import com.tokopedia.mediauploader.ui.component.LoadImage
import com.tokopedia.mediauploader.ui.component.LogItem
import com.tokopedia.mediauploader.ui.component.ProgressLoaderItem
import com.tokopedia.mediauploader.ui.utils.launchMediaPicker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun DebugScreen(viewModel: DebugMediaUploaderViewModelContract) {
    var hasBrowseFile by remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    ConfigBottomSheet(viewModel) {
        Surface(color = Color.White) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val (
                    imagePreview,
                    logList,
                    browseButton,
                    configButton,
                    uploadButton,
                    progressLoader
                ) = createRefs()

                val launcher = launchMediaPicker(viewModel) {
                    hasBrowseFile = true
                }

                Box(
                    modifier = Modifier
                        .background(color = NestTheme.colors.NN._950)
                        .fillMaxWidth()
                        .height(240.dp)
                        .constrainAs(imagePreview) {
                            top.linkTo(parent.top)
                        }
                )

                AnimatedVisibility(hasBrowseFile) {
                    val file = state.filePath.firstOrNull()
                    if (file != null) LoadImage(file.toString())
                }

                ProgressLoaderItem(
                    state.progress.first.toString(),
                    state.progress.second,
                    Modifier
                        .padding(16.dp)
                        .constrainAs(progressLoader) {
                            bottom.linkTo(imagePreview.bottom)
                        }
                )

                ConfigButton(
                    modifier = Modifier
                        .constrainAs(configButton) {
                            end.linkTo(browseButton.start)
                            bottom.linkTo(imagePreview.bottom)
                        },
                    onClick = {
                        scope.launch {
                            if (it.isVisible) {
                                it.hide()
                            } else {
                                it.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        }
                    }
                )

                BrowseFileButton(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .constrainAs(browseButton) {
                            bottom.linkTo(imagePreview.bottom)
                            end.linkTo(imagePreview.end)
                        },
                    onClick = {
                        launcher.launch(it)
                    }
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .constrainAs(logList) {
                            top.linkTo(imagePreview.bottom)
                            bottom.linkTo(uploadButton.top)
                            height = Dimension.fillToConstraints
                        }
                ) {
                    items(state.logs.reversed()) {
                        val (type, logs) = it

                        LogItem(
                            type = LogType.map(type),
                            logs = logs,
                            modifier = Modifier
                                .animateItemPlacement()
                        )
                    }
                }

                NestButton(
                    text = "Upload",
                    modifier = Modifier
                        .padding(8.dp)
                        .constrainAs(uploadButton) {
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    viewModel.setAction(DebugMediaLoaderEvent.Upload)
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun DebugScreenPreview() {
    DebugScreen(debugViewModel)
}

val debugViewModel = object : DebugMediaUploaderViewModelContract {
    override val state: StateFlow<DebugMediaLoaderState> =
        MutableStateFlow(DebugMediaLoaderState().also {
            it.log(LogType.Welcome, listOf(Logs("Foo", "Bar")))
        })

    override val config: StateFlow<DebugUploaderParam>
        get() = MutableStateFlow(DebugUploaderParam.default())

    override fun setAction(event: DebugMediaLoaderEvent) = Unit
    override fun setSourceId(value: String) = Unit
    override fun shouldCompress(status: Boolean) = Unit
    override fun waitingTranscode(status: Boolean) = Unit
}

