import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from './store/store'
import App from './components/App'


class Root extends Component {
  render() {
    return (
    <Provider store={store}>
      <App screenProps={this.props.screenProps} />
    </Provider>
    )
  }
}

export default Root
