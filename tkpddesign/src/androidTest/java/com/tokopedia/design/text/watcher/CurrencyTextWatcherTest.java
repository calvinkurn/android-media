package com.tokopedia.design.text.watcher;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

// @SmallTest
// @RunWith(AndroidJUnit4.class)
// public class CurrencyTextWatcherTest {
//     private CurrencyTextWatcher currencyTextWatcher;
//     private CurrencyTextWatcher.OnNumberChangeListener onNumberChangeListener;

//     @Before
//     public void setup() {
//         final EditText editText = mock(EditText.class);
//         currencyTextWatcher = new CurrencyTextWatcher(editText);
//         onNumberChangeListener = mock(CurrencyTextWatcher.OnNumberChangeListener.class);
//         currencyTextWatcher.setOnNumberChangeListener(onNumberChangeListener);
//     }

//     @Test
//     public void testOnTextChanged_callsProcess() {
//         final Editable editable = mock(Editable.class);
//         when(editable.toString()).thenReturn("1,000");
//         currencyTextWatcher.afterTextChanged(editable);

//         verify(onNumberChangeListener, times(1)).onNumberChanged(eq(1000D));

//     }

// }