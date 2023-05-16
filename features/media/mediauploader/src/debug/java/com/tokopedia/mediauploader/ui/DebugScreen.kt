package com.tokopedia.mediauploader.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import coil.fetch.VideoFrameUriFetcher
import coil.request.videoFrameMillis
import com.tokopedia.common_compose.components.NestButton
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.mediauploader.DebugMediaLoaderEvent
import com.tokopedia.mediauploader.DebugMediaLoaderState
import com.tokopedia.mediauploader.LogType
import com.tokopedia.mediauploader.DebugMediaUploaderViewModelContract
import com.tokopedia.picker.common.MediaPicker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun String.isVideo(): Boolean {
    return contains(".mp4", ignoreCase = true)
}

@Composable
fun launchMediaPicker(
    viewModel: DebugMediaUploaderViewModelContract,
    content: () -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
) {
    if (it.data != null) {
        val result = MediaPicker.result(it.data)

        if (result.originalPaths.isNotEmpty()) {
            viewModel.setAction(DebugMediaLoaderEvent.FileChosen(result.originalPaths))
        }

        content()
    }
}

@Composable
@Suppress("OPT_IN_USAGE")
fun LoadImage(file: String) {
    val context = LocalContext.current

    Image(
        painter = rememberImagePainter(
            data = file,
            builder = {
                if (file.isVideo()) fetcher(VideoFrameUriFetcher(context))
                videoFrameMillis(1000)
            }
        ),
        contentDescription = "local image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    )
}

@Composable
inline fun BrowseFileButton(
    modifier: Modifier = Modifier,
    crossinline onClick: (Intent) -> Unit
) {
    val context = LocalContext.current

    Button(
        shape = RoundedCornerShape(65.dp),
        colors = ButtonDefaults.buttonColors(
            NestTheme.colors.NN._0
        ),
        modifier = modifier
            .padding(10.dp),
        onClick = {
            onClick(MediaPicker.intent(context) {
                singleSelectionMode()
                maxVideoFileSize(262144000)
                maxVideoDuration(60000)
            })
        }
    ) {
        Text(
            text = "Browse",
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            modifier = Modifier
        )
    }
}

@Composable
fun LogItem(type: String, content: String) {
    Column(
        modifier = Modifier
            .border(border = BorderStroke(0.5.dp, NestTheme.colors.NN._300), shape = RoundedCornerShape(8.dp))
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        NestTypography(
            textStyle = NestTheme.typography.display3,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = NestTheme.colors.NN._600)) {
                    append(type)
                }
            }
        )

        Spacer(modifier = Modifier.height(2.dp))

        NestTypography(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = NestTheme.colors.NN._800
                    )
                ) {
                    append(content)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ProgressLoaderItem(type: String, value: Int, modifier: Modifier = Modifier) {
    NestTypography(
        text = buildAnnotatedString {
            withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                withStyle(style = SpanStyle(color = NestTheme.colors.NN._600)) {
                    append("$type: ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = NestTheme.colors.GN._500
                    )
                ) {
                    append("$value%")
                }
            }
        },
        modifier
    )
}

@Composable
fun DebugScreen(viewModel: DebugMediaUploaderViewModelContract) {
    val state by viewModel.state.collectAsState()
    var hasBrowseFile by remember { mutableStateOf(false) }

    Surface(color = Color.White) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            val (
                imagePreview,
                logList,
                browseButton,
                uploadButton,
                progressLoader,
                configButton
            ) = createRefs()

            val launcher = launchMediaPicker(viewModel) {
                hasBrowseFile = true
            }

            Box(
                modifier = Modifier
                    .background(color = Color.Gray)
                    .fillMaxWidth()
                    .height(240.dp)
                    .constrainAs(imagePreview) {
                        top.linkTo(parent.top)
                    }
            )

            AnimatedVisibility(hasBrowseFile) {
                val file = state.filePaths.firstOrNull()
                if (file != null) LoadImage(file)
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

            BrowseFileButton(
                modifier = Modifier
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
                    val (type, content) = it
                    LogItem(type = LogType.map(type), content = content)
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

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun DebugScreenPreview() {
    val debugViewModel = object : DebugMediaUploaderViewModelContract {
        override val state: StateFlow<DebugMediaLoaderState> = MutableStateFlow(DebugMediaLoaderState())
        override fun setAction(event: DebugMediaLoaderEvent) = Unit
    }

    DebugScreen(debugViewModel)
}

@Preview(showBackground = true)
@Composable
fun LogItemPreview() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(8.dp)
    ) {
        item {
            LogItem(
                type = LogType.map(LogType.CompressInfo),
                content = buildString {
                    append("foo: bar")
                    append("\n")
                    append("loren: ipsum")
                }
            )
        }
    }
}
