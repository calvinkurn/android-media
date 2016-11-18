package com.tokopedia.core.myproduct.model;

import org.parceler.Parcel;

/**
 * Created by Toped18 on 6/8/2016.
 */

@Parcel(parcelsIndex = false)
public class CreateShopNoteParam {
    String noteContent;
    String noteId;
    String noteTitle;
    String terms;

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }
}
