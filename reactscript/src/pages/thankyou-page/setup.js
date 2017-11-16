import React, { Component } from 'react'
import { Provider } from 'react-redux'
import App from './App'
import Store from './store/Store'


export default class Root extends Component {
  render() {
    return(
      <Provider store={Store}>
        <App data={this.props.data} />
      </Provider>
    )
  }
}
