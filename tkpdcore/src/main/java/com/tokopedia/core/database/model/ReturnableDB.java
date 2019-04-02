package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.DatabaseConstant;

/**
 * Created by m.normansyah on 12/27/15.
 * modified by m.normansyah on 6/10/2016
 */
@ModelContainer
@Table(database = DbFlowDatabase.class)
public class ReturnableDB extends BaseModel implements DatabaseConstant {

    public static final String NOTE_TITLE = "note_title";
    public static final String NOTE_TEXT = "note_text";
    public static final String NOTE_ID = "note_id";
    public static final String PENGEMBALIAN = "Kebijakan Pengembalian Produk";

    public ReturnableDB(){
        super();
    }

    public ReturnableDB(String title, String noteText, int noteId) {
        super();
        this.noteTitle = title;
        this.noteText = noteText;
        this.noteId = noteId;
    }

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Override
    public long getId() {
        return Id;
    }

    @Column(name = NOTE_TITLE)
    public String noteTitle;

    @Column(name = NOTE_TEXT)
    public String noteText;

    @Column(name = NOTE_ID)
    public int noteId;

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}
