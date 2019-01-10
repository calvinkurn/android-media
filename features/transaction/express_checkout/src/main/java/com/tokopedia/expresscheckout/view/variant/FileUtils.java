package com.tokopedia.expresscheckout.view.variant;

import android.content.Context;

import com.tokopedia.expresscheckout.view.errorview.ErrorBottomsheets;
import com.tokopedia.expresscheckout.view.errorview.ErrorBottomsheetsActionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

    public String readRawTextFile(Context ctx, int resId) {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        ErrorBottomsheets a = new ErrorBottomsheets();
        a.setActionListener(new ErrorBottomsheetsActionListener() {
            @Override
            public void onActionButtonClicked() {

            }
        });
        return text.toString();
    }

}

