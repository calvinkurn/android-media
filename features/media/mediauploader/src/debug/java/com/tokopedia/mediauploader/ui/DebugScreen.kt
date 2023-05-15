package com.tokopedia.mediauploader.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.fetch.VideoFrameUriFetcher
import com.tokopedia.common_compose.components.NestButton
import com.tokopedia.common_compose.principles.NestImage
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.mediauploader.DebugMediaLoaderEvent
import com.tokopedia.mediauploader.LogType
import com.tokopedia.mediauploader.MediaUploaderStateManager
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.image.ImageUploaderManager
import com.tokopedia.mediauploader.video.data.entity.VideoInfo
import com.tokopedia.mediauploader.video.data.repository.VideoMetaDataExtractorRepository
import com.tokopedia.picker.common.MediaPicker
import java.io.File

fun String.isVideo(): Boolean {
    return contains(".mp4", ignoreCase = true)
}

@Composable
fun launchMediaPicker(
    viewModel: MediaUploaderStateManager?,
    content: () -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
) {
    val result = MediaPicker.result(it.data)

    viewModel?.setAction(
        DebugMediaLoaderEvent.FileResult(result.originalPaths)
    )

    content()
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
            .border(border = BorderStroke(0.5.dp, NestTheme.colors.NN._300))
            .fillMaxWidth()
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        NestTypography(
            textStyle = NestTheme.typography.display3,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = NestTheme.colors.NN._600)) {
                    append(type)
                }
            },
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
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun DebugScreen(viewModel: MediaUploaderStateManager) {
    val context = LocalContext.current
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

                if (file != null) {
                    Image(
                        painter = rememberImagePainter(
                            data = File(file),
                            builder = {
                                if (file.isVideo()) {
                                    fetcher(VideoFrameUriFetcher(context))
                                }
                            }
                        ),
                        contentDescription = "local image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    )
                }
            }

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
                items(state.logs) {
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
                viewModel?.setAction(DebugMediaLoaderEvent.Upload)
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
//    DebugScreen()
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
                type = LogType.map(LogType.Progress("compress")),
                content = buildString {
                    append("foo: bar")
                    append("\n")
                    append("loren: ipsum")
                }
            )
        }
        item {
            LogItem(
                type = LogType.map(LogType.Progress("upload")),
                content = buildString {
                    append("foo: bar")
                    append("\n")
                    append("loren: ipsum")
                }
            )
        }
    }
}
