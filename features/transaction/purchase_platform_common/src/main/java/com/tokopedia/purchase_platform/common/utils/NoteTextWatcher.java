package com.tokopedia.purchase_platform.common.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Irfan Khoirul on 18/07/18.
 */

public class NoteTextWatcher implements TextWatcher {

    public static final int TEXTWATCHER_NOTE_DEBOUNCE_TIME = 100;

    private NoteTextwatcherListener noteTextwatcherListener;

    public NoteTextWatcher(NoteTextwatcherListener noteTextwatcherListener) {
        this.noteTextwatcherListener = noteTextwatcherListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (noteTextwatcherListener != null) {
            noteTextwatcherListener.onNoteChanged(editable);
        }
    }

    public interface NoteTextwatcherListener {
        void onNoteChanged(Editable editable);
    }

}
