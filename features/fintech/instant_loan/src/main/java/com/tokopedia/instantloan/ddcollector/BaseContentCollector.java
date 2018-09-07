//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseContentCollector extends BaseCollector {
    private ContentResolver mContentResolver;

    public BaseContentCollector(ContentResolver contentResolver) {
        this.mContentResolver = contentResolver;
    }

    public abstract List<String> getParameters();

    public abstract int getLimit();

    public abstract Uri buildUri();

    public Object getData() {
        return this.collect(this.buildUri());
    }

    public List<Map<String, String>> collect(Uri uri) {
        List<Map<String, String>> phoneInfoColumn = new ArrayList<>();
        String sortOrder = null;

        if (getLimit() > 0) {
            sortOrder = String.format("%s limit " + getLimit(), BaseColumns._ID);
        }

        Cursor cursor = this.mContentResolver.query(uri, this.getParameters().toArray(new String[0]), null, null, sortOrder);
        if (cursor != null && cursor.getCount() > 0) {
            List<String> paramList = this.getParameters();
            cursor.moveToFirst();

            do {
                Map<String, String> phoneInfoMap = new HashMap<>();

                for (String columnName : paramList) {
                    String value = cursor.getString(cursor.getColumnIndex(columnName));
                    phoneInfoMap.put(columnName, value);
                }

                phoneInfoColumn.add(phoneInfoMap);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return phoneInfoColumn;
    }
}
