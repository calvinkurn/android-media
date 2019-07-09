package com.tokopedia.core.database.repository;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.database.dao.ResCenterAttachmentDao;
import com.tokopedia.core.database.manager.ResCenterAttachmentDatabase;
import com.tokopedia.core.database.model.ResCenterAttachment;

public class ResCenterAttachmentRepository {

    private ResCenterAttachmentDao resCenterAttachmentDao;

    public ResCenterAttachmentRepository(Application application) {
        ResCenterAttachmentDatabase db = ResCenterAttachmentDatabase.getDatabase(application);
        this.resCenterAttachmentDao = db.resCenterAttachmentDao();
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
    };
}
