package com.tokopedia.mediauploader.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestTextField
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConfigBottomSheet(
    sourceId: String,
    setSourceId: (String) -> Unit,
    setShouldCompress: (Boolean) -> Unit,
    setWaitingTranscode: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (ModalBottomSheetState) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch {
            sheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = { ConfigBottomSheetContent(
            sourceId = sourceId,
            setSourceId = setSourceId,
            setShouldCompress = setShouldCompress,
            setWaitingTranscode = setWaitingTranscode
        ) },
        modifier = modifier.fillMaxSize()
    ) {
        content(sheetState)
    }
}

@Composable
fun ConfigBottomSheetContent(
    sourceId: String,
    setSourceId: (String) -> Unit,
    setShouldCompress: (Boolean) -> Unit,
    setWaitingTranscode: (Boolean) -> Unit
) {
    // these variable aren't hoisted cause we only need to handle the checkbox status
    var shouldCompress by remember { mutableStateOf(false) }
    var waitingTranscode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        NestTypography(
            textStyle = NestTheme.typography.heading4,
            text = "Config"
        )

        Spacer(modifier = Modifier.height(24.dp))

        NestTextField(
            value = sourceId,
            modifier = Modifier
                .fillMaxWidth(),
            label = "Source ID",
            onValueChanged = { text ->
                setSourceId(text)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = shouldCompress,
                    onCheckedChange = { checked ->
                        shouldCompress = checked
                        setShouldCompress(checked)
                    })

                Text(
                    text = "Compress Video",
                    style = MaterialTheme.typography.body2
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = waitingTranscode,
                    onCheckedChange = { checked ->
                        waitingTranscode = checked
                        setWaitingTranscode(checked)
                    })

                Text(
                    text = "Waiting Transcode",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigBottomSheetPreview() {
    ConfigBottomSheetContent(
        sourceId = "abc",
        setSourceId = {},
        setShouldCompress = {},
        setWaitingTranscode = {}
    )
}
