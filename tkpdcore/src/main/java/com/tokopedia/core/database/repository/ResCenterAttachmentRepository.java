package com.tokopedia.core.database.repository;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.database.dao.ResCenterAttachmentDao;
import com.tokopedia.core.database.AppDatabase;
import com.tokopedia.core.database.model.ResCenterAttachment;

import java.util.List;

public class ResCenterAttachmentRepository {

    private ResCenterAttachmentDao resCenterAttachmentDao;

    public ResCenterAttachmentRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.resCenterAttachmentDao = db.resCenterAttachmentDao();
    }

    public void deleteAttachments(List<ResCenterAttachment> resCenterAttachments) {

    }

    public static final Parcelable.Creator<ResCenterAttachment> CREATOR = new Parcelable.Creator<ResCenterAttachment>() {
        @Override
        public ResCenterAttachment createFromParcel(Parcel parcel) {
            return new ResCenterAttachment(parcel);
        }

        @Override
        public ResCenterAttachment[] newArray(int size) {
            return new ResCenterAttachment[size];
        }
    }
}
