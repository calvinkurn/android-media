package com.tokopedia.core.database.repository;

import android.app.Application;

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
        this.resCenterAttachmentDao.deleteAttachments(resCenterAttachments);
    }
}
