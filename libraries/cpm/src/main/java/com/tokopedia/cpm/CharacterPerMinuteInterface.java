package com.tokopedia.cpm;

import android.support.annotation.NonNull;

public interface CharacterPerMinuteInterface {
    String KEY = "CPM";

    void saveCPM(@NonNull String cpm);

    String getCPM();

    boolean isEnable();
}
