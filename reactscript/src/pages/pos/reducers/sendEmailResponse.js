import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import {
  SEND_EMAIL,
} from '../actions/index'



export const sendEmailResponse = (state = {
    isSuccess: false,
    isSending: false,
    message: []
  }, action) => {
    switch (action.type) {
      case `${SEND_EMAIL}_${PENDING}`:
        return {
          ...state,
          isSending: true,
          isSuccess: false
        }
      case `${SEND_EMAIL}_${FULFILLED}`:
        return {
          ...state,
          isSending: false,
          isSuccess: true,
          message: action.payload
        }
      case `${SEND_EMAIL}_${REJECTED}`:
        return {
          ...state,
          isSending: false,
          isSuccess: false
        }
  
      default:
        return state
    }
  }