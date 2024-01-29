---
title: "SSE (Server-Sent Event)"
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 30 Sep 2022 / <!--start status:BLUE-->MA-3.194<!--end status--> <!--start status:GREEN-->SA-2.124<!--end status-->  |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->  |
| Module Location | `libraries.sse` | `/libraries/sse` |

## Table of Contents

<!--toc-->

## Release Notes (max 5 latest release notes)

<!--start expand:16 Nov 22 (MA-3.202/SA-2.132)-->
Fix SSE leaking on Play Upcoming Room

PR : <https://github.com/tokopedia/android-tokopedia-core/pull/30359>   
JIRA : 

 

 




 
 [AN-44225](https://tokopedia.atlassian.net/browse/AN-44225)
 -
 Getting issue details...

STATUS
<!--end expand-->

## Overview

SSE (Server-Sent Events) is a [server push](https://en.wikipedia.org/wiki/Server_push) technology **enabling client to receive automatic updates from a server via an HTTP connection** and describes how servers can initiate data transmission towards clients once an initial client connection has been established. They are commonly used to send message updates or continuous data streams to a browser client and are designed to enhance native, cross-browser streaming through a JavaScript API called EventSource, through which a client requests a particular URL in order to receive an event stream. **SSE is quite similar to WebSocket, except the communication is mono-directional, while WebSocket is bi-directional.**

### Background

We do need SSE to get real-time data from the server without hitting the server over and over again. This technology can be useful if you want to implement something like updating status, a news feed, etc.

In the Play module, we use SSE for getting upcoming channel status when the channel is already alive. We are using this technology since <!--start status:GREEN-->MA-3.145<!--end status--> (

 

 




 
 [CNGC-2145](https://tokopedia.atlassian.net/browse/CNGC-2145)
 -
 Getting issue details...

STATUS




) and so far everything is good 👌

### Project Description

This library consists of base classes & logic of SSE handling. You need to handle when the connection is built and when the connection is closed, please don’t forget to always close the connection when user is leaving your page to avoid any leaking.

If your feature only needs 1-directional real time communication from server to client, you may consider to use SSE.

## Tech Stack

- OkHttp : SSE is built on top of OkHttp. SSE opens the connection with OkHttp and keep it alive until client closes the connection / some error occurs.

## Flow Diagram

<img src="https://docs-android.tokopedia.net/images/docs/res/SSE.drawio-20230104-055935.png" alt="" />

## How-to

#### (1) Add SSE library dependency



```
dependencies {
  ...
  implementation projectOrAar(rootProject.ext.libraries.sse)
  ...
}
```

#### (2) Connect to SSE



```
fun connectSSE() {
  /**
    * You can use `TokopediaUrl.getInstance().SSE` as the base URL
    * Production : https://sse.tokopedia.com/
    * Staging    : https://sse-staging.tokopedia.com/
    */
  var url = "your_sse_url"

  val request = Request.Builder().get().url(url)
      .header("Origin", TokopediaUrl.getInstance().WEB)
      .header("Accounts-Authorization", "Bearer ${userSession.accessToken}")
      .header("X-Device", "android-" + GlobalConfig.VERSION_NAME)
      .header(HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX)
      .build()
  
  sse = OkSse().newServerSentEvent(request, object: ServerSentEvent.Listener {
      override fun onOpen(sse: ServerSentEvent, response: Response) {
        /** This callback will be triggered when first data is received */
      }
  
      override fun onMessage(
          sse: ServerSentEvent,
          id: String,
          event: String,
          message: String
      ) {
        /** Most of the time your data will be come from this callback */
      }
  
      override fun onComment(sse: ServerSentEvent, comment: String) { 
        /** This callback will be triggered if your data is not in form of:
          * xxx : yyy
          */
      }
  
      override fun onRetryTime(sse: ServerSentEvent, milliseconds: Long): Boolean {
          return true
      }
  
      override fun onRetryError(
          sse: ServerSentEvent,
          throwable: Throwable,
          response: Response?
      ): Boolean {
          return true
      }
  
      override fun onClosed(sse: ServerSentEvent) {
        /**
          * Handle if SSE is closed.
          */
      }
  
      override fun onPreRetry(sse: ServerSentEvent, originalRequest: Request): Request? {
          return request
      }
  })
}
```

#### (3) Close SSE

Close SSE when the user leaves your page / its not needed anymore.



```
fun closeSSE() {
  sse?.close()
}
```



---

## Action Items

- [Improvement] Allowing user to override `OkHttpClient` inside `OkSse` for adding additional attributes e.g. authentication, etc
- [Improvement] Set maxRetry capability to save bandwidth

## Useful Links

- [AN-41235](https://tokopedia.atlassian.net/browse/AN-41235)
 -
 Getting issue details...

STATUS
- [[DRAFT][Tech] Implementation Server-Sent Event in Content](/wiki/spaces/CN/pages/1348180371)

## FAQ

<!--start expand:What is the supported response format for SSE?-->
The current SSE implementation only accepts this response format :



```
id: 1621478921
event: upcommingchannelupdateactive
data:{"channel_id":10264}
retry: 100
```

- These 4 fields are used & mandatory.
- In most cases, you gonna use `event` & `data` field only.
- Note that `data` field is a string, but you can parse it into JSON later in your mapper.
<!--end expand-->

<!--start expand:If I have questions / feedbacks about SSE, who is the PIC?-->
If you have any questions, encounter any errors or have feedback don’t hesitate to reach out to [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) team!
<!--end expand-->

