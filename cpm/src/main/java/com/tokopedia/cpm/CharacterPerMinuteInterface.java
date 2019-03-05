package com.tokopedia.cpm;

import android.support.annotation.NonNull;

public interface CharacterPerMinuteInterface {
    void saveCPM(@NonNull String cpm);

    String getCPM();

    boolean isEnable();
}
