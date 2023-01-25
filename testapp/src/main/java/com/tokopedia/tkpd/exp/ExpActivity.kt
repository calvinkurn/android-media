package com.tokopedia.tkpd.exp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestButton
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

class ExpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NestTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val darkMode by remember { mutableStateOf(getDarkModeStatus()) }
                    Column {
                        Row(
                            modifier = Modifier
                                .weight(1.0f)
                                .horizontalScroll(rememberScrollState())
                        ) {
                            val widthEachColumn = 125.dp
                            Column(
                                modifier = Modifier
                                    .defaultMinSize(minWidth = widthEachColumn)
                                    .padding(4.dp)
                            ) {
                                ColumnHeader(text = "ORGANIC")
                                Text(text = "Heading 3", style = NestTheme.typography.heading3)
                                Text(text = "Display 3", style = NestTheme.typography.display3)
                                Text(text = "Body 1", style = NestTheme.typography.body1)
                                Button(onClick = {}) {
                                    Text(
                                        text = "Click me!",
                                        color = Color.White,
                                        style = NestTheme.typography.display2,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            ExpDivider()
                            Column(
                                modifier = Modifier
                                    .defaultMinSize(minWidth = widthEachColumn)
                                    .padding(4.dp)
                            ) {
                                ColumnHeader(text = "NEST CUSTOM")
                                NestTypography(
                                    text = "Heading 3",
                                    textStyle = NestTheme.typography.heading3
                                )
                                NestTypography(
                                    text = "Display 3",
                                    textStyle = NestTheme.typography.display3
                                )
                                NestTypography(
                                    text = "Body 1",
                                    textStyle = NestTheme.typography.body1
                                )
                                NestButton(text = "Click me!") { }
                            }
                            ExpDivider()
                            Column(
                                modifier = Modifier
                                    .defaultMinSize(minWidth = widthEachColumn)
                                    .padding(4.dp)
                            ) {
                                ColumnHeader(text = "VIEWS IN COMPOSE")
                                TextUnify(
                                    text = "Heading 3",
                                    type = TextUnifyType.Heading3,
                                    update = {}
                                )
                                TextUnify(
                                    text = "Display 3",
                                    type = TextUnifyType.Display3,
                                    update = {}
                                )
                                TextUnify(text = "Body 1", type = TextUnifyType.Body1, update = {})
                                UnifyButtonCompose(text = "Click me!")
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = darkMode, onCheckedChange = {
                                setDarkModeAndRecreate(darkMode.not())
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Dark Mode")
                        }
                    }
                }
            }
        }
    }

    private fun setDarkModeAndRecreate(active: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (active) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(Intent(this, this.javaClass))
    }

    private fun getDarkModeStatus(): Boolean =
        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}
