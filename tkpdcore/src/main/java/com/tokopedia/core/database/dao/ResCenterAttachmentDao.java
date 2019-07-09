package com.tokopedia.core.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.tokopedia.core.database.model.ResCenterAttachment;

import java.util.List;

@Dao
public interface ResCenterAttachmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAttachment(ResCenterAttachment resCenterAttachment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAttachments(List<ResCenterAttachment> resCenterAttachment);
}
