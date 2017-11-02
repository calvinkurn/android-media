
export const SEND_EMAIL = 'SEND_EMAIL'
export const sendEmail = (data) => {
  console.log("send email")
  return {
    type: SEND_EMAIL,
    payload: apiSendEmail(data)
  }
}

const apiSendEmail = (data) => {
  return data
}

