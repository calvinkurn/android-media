package com.tokopedia.core.database.repository;

import android.content.Context;

import com.tokopedia.core.database.dao.ResCenterAttachmentDao;
import com.tokopedia.core.database.AppDatabase;
import com.tokopedia.core.database.model.ResCenterAttachment;

import java.util.List;

public class ResCenterAttachmentRepository {

    private ResCenterAttachmentDao resCenterAttachmentDao;

    public ResCenterAttachmentRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        this.resCenterAttachmentDao = db.resCenterAttachmentDao();
    }

    public void insertAttachment(ResCenterAttachment resCenterAttachment) {
        resCenterAttachmentDao.insertAttachment(resCenterAttachment);
    }

    public void deleteAttachments(List<ResCenterAttachment> resCenterAttachments) {
        resCenterAttachmentDao.deleteAttachments(resCenterAttachments);
    }

    public List<ResCenterAttachment> getAttachmentListByResIdModuleName(String resId, String moduleName) {
        return resCenterAttachmentDao.getAttachmentListByResIdModuleName(resId, moduleName);
    }

    public void deleteAttachmentById(long id) {
        resCenterAttachmentDao.deleteAttachmentById(id);
    }
}
