package com.tokopedia.tkpd

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.components.NestButton
import com.tokopedia.common_compose.principles.NestHeader
import com.tokopedia.common_compose.ui.NestTheme

@SuppressLint("UnsupportedDarkModeColor")
@Composable
fun HomeScreen(
    model: MainActivity.Model = MainActivity.Model(),
    onDarkModeChanged: () -> Unit = {},
    onApplinkChanged: (String) -> Unit = {},
    onNavigateTo: (MainActivity.HomeDestination) -> Unit = {}
) {
    Surface {
        Column {
            NestHeader(title = "Tokopedia Test App", showBackIcon = false)
            val urlBgColor = if (model.urlState.contains("live", true)) {
                NestTheme.colors.GN._600
            } else {
                NestTheme.colors.YN._600
            }
            Text(
                text = model.urlState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(urlBgColor)
                    .padding(vertical = 2.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary,
                style = NestTheme.typography.heading6
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 72.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateTo(MainActivity.HomeDestination.LOGIN) }
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = model.loginText,
                        textAlign = TextAlign.Center,
                        style = NestTheme.typography.display1.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (model.isLoggedIn) {
                    NestButton(
                        text = "Logout",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onNavigateTo(MainActivity.HomeDestination.LOGOUT) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                NestButton(
                    text = "Developer Option",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateTo(MainActivity.HomeDestination.DEVELOPER_OPTION) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                NestButton(
                    text = "Login Helper",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateTo(MainActivity.HomeDestination.LOGINHELPER) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = model.applink,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    onValueChange = onApplinkChanged,
                    textStyle = NestTheme.typography.body3,
                    label = {
                        Text(text = "applink")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                NestButton(
                    text = "Open",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateTo(MainActivity.HomeDestination.APPLINK) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onDarkModeChanged() }
                        .padding(8.dp)
                ) {
                    Checkbox(checked = model.isDarkModeChecked, onCheckedChange = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Force Dark Mode")
                }
            }
        }
    }
}

@Preview
@Composable
fun TestAppHomePreview() {
    NestTheme {
        HomeScreen()
    }
}
