package com.tokopedia.videoplayer.state

import com.tokopedia.videoplayer.R
import org.junit.Assert.assertEquals
import org.junit.Test

class VideoSourceProtocolTest {

    private lateinit var protocol: VideoSourceProtocol

    @Test fun `should return valid http protocol`() {
        val source = "http://source.com/file.mp4"
        protocol = VideoSourceProtocol.protocol(source)
        assert(protocol == VideoSourceProtocol.Http)
    }

    @Test fun `should return valid https protocol`() {
        val source = "https://source.com/file.mp4"
        protocol = VideoSourceProtocol.protocol(source)
        assert(protocol == VideoSourceProtocol.Http)
    }

    @Test fun `should return valid rtmp protocol`() {
        val source = "rtmp://source.com/file.mp4"
        protocol = VideoSourceProtocol.protocol(source)
        assert(protocol == VideoSourceProtocol.Rtmp)
    }

    @Test fun `should return valid file path`() {
        val source = "file://source.com/file.mp4"
        protocol = VideoSourceProtocol.protocol(source)
        assert(protocol == VideoSourceProtocol.File)
    }

    @Test fun `should return invalid format`() {
        val source = "\"file://source.com/file.mp4\""
        protocol = VideoSourceProtocol.protocol(source)
        assertEquals(protocol, VideoSourceProtocol.InvalidFormat(R.string.videoplayer_invalid_protocol_type))
        //assert(protocol == VideoSourceProtocol.InvalidFormat(0))
    }

    @Test fun `should return invalid protocol`() {
        val source = "https//source.com/file.mp4"
        protocol = VideoSourceProtocol.protocol(source)
        assertEquals(protocol, VideoSourceProtocol.InvalidFormat(R.string.videoplayer_invalid_protocol_format))
        //assert(protocol == VideoSourceProtocol.InvalidFormat(0))
    }

}