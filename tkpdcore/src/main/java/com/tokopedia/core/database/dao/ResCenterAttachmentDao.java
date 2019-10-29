package com.tokopedia.core.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tokopedia.core.database.DbMetadata;
import com.tokopedia.core.database.model.ResCenterAttachment;

import java.util.List;

@Dao
public interface ResCenterAttachmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAttachment(ResCenterAttachment resCenterAttachment);

    @Delete
    void deleteAttachments(List<ResCenterAttachment> resCenterAttachments);

    @Delete
    void deleteAttachment(ResCenterAttachment resCenterAttachment);

    @Query("SELECT * FROM " + DbMetadata.resCenterTableName + " WHERE resolutionId = :resId AND moduleName = :moduleName")
    List<ResCenterAttachment> getAttachmentListByResIdModuleName(String resId, String moduleName);

    @Query("DELETE FROM " + DbMetadata.resCenterTableName + " WHERE id = :id")
    void deleteAttachmentById(long id);
}
