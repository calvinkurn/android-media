package com.tokopedia.tkpd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                    Row {
                        Column(modifier = Modifier.weight(1.0f).padding(4.dp)) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp, bottom = 32.dp),
                                text = "ORGANIC",
                                style = NestTheme.typography.heading6,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(text = "Heading 3", style = NestTheme.typography.heading3)
                            Text(text = "Display 3", style = NestTheme.typography.display3)
                            Text(text = "Body 1", style = NestTheme.typography.body1)
                            Button(onClick = {}) {
                                Text(text = "Click me!")
                            }
                        }
                        Divider(modifier = Modifier.fillMaxHeight().width(1.dp), color = Color.LightGray)
                        Column(modifier = Modifier.weight(1.0f).padding(4.dp)) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp, bottom = 32.dp),
                                text = "NEST CUSTOM",
                                style = NestTheme.typography.heading6,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            NestTypography(text = "Heading 3", textStyle = NestTheme.typography.heading3)
                            NestTypography(text = "Display 3")
                            NestTypography(text = "Body 1", textStyle = NestTheme.typography.body1)
                            NestButton(text = "Click me!") { }
                        }
                    }
                }
            }
        }
    }
}
