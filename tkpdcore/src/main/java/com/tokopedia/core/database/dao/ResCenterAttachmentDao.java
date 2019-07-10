package com.tokopedia.core.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.tokopedia.core.database.model.ResCenterAttachment;

import java.util.List;

@Dao
public interface ResCenterAttachmentDao {

    @Delete
    void deleteAttachments(List<ResCenterAttachment> resCenterAttachments);
}
